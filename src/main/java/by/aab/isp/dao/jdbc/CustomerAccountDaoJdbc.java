package by.aab.isp.dao.jdbc;

import by.aab.isp.dao.CustomerAccountDao;
import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.TariffDao;
import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.CustomerAccount;
import by.aab.isp.entity.User;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public final class CustomerAccountDaoJdbc extends AbstractRepositoryJdbc<CustomerAccount> implements CustomerAccountDao {

    private static final List<String> FIELDS = List.of("tariff_id", "balance", "permitted_overdraft", "payoff_date");

    private final TariffDao tariffDao;
    private final UserDao userDao;

    public CustomerAccountDaoJdbc(DataSource dataSource, TariffDao tariffDao, UserDao userDao) {
        super(dataSource, "customer_accounts", FIELDS);
        sqlInsert = "INSERT INTO " + tableName
                + "(user_id,"
                + FIELDS.stream()
                        .reduce(new StringJoiner(",", "", ")"),
                                StringJoiner::add,
                                StringJoiner::merge)
                + "VALUES (?,"
                + FIELDS.stream()
                .map(field -> "?")
                .reduce(new StringJoiner(",", "(", ")"),
                        StringJoiner::add,
                        StringJoiner::merge);
        sqlSelectWhereId = sqlSelect + " WHERE user_id=";
        sqlUpdateWhereId = sqlUpdate + " WHERE user_id=";
        this.tariffDao = tariffDao;
        this.userDao = userDao;
    }

    final String sqlCountWhereUserId = sqlCount + " WHERE user_id=";

    @Override
    public long countByUserId(long userId) {
        return count(sqlCountWhereUserId + userId);
    }

    @Override
    public Optional<CustomerAccount> findByUser(User user) {
        return findOne(sqlSelectWhereId + user.getId(), row -> {
            try {
                long tariffId = row.getLong("tariff_id");
                Timestamp payoffDate = row.getTimestamp("payoff_date");
                return new CustomerAccount(
                        user,
                        tariffId != 0 ? tariffDao.findById(tariffId).orElseThrow()
                                      : null,
                        row.getBigDecimal("balance"),
                        row.getBigDecimal("permitted_overdraft"),
                        payoffDate != null ? payoffDate.toInstant()
                                           : null);
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        });
    }

    @Override
    void mapObjectToRow(CustomerAccount account, PreparedStatement row) {
        try {
            int c = 0;
            row.setLong(++c, account.getId());
            row.setLong(++c, account.getTariff() != null ? account.getTariff().getId()
                                                         : 0);
            row.setBigDecimal(++c, account.getBalance());
            row.setBigDecimal(++c, account.getPermittedOverdraft());
            row.setTimestamp(++c, account.getPayoffDate() != null ? Timestamp.from(account.getPayoffDate())
                                                                  : null);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    CustomerAccount mapRowToObject(ResultSet row) {
        try {
            long userId = row.getLong("user_id");
            long tariffId = row.getLong("tariff_id");
            Timestamp payoffDate = row.getTimestamp("payoff_date");
            return new CustomerAccount(
                    userDao.findById(userId).orElseThrow(),
                    tariffId != 0 ? tariffDao.findById(tariffId).orElseThrow()
                                  : null,
                    row.getBigDecimal("balance"),
                    row.getBigDecimal("permitted_overdraft"),
                    payoffDate != null ? payoffDate.toInstant()
                                       : null
            );
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    CustomerAccount objectWithId(CustomerAccount account, long id) {
        throw new RuntimeException("This should never happen");
    }
}
