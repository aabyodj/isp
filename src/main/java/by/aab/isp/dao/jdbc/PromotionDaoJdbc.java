package by.aab.isp.dao.jdbc;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.PromotionDao;
import by.aab.isp.entity.Promotion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

public class PromotionDaoJdbc extends AbstractRepositoryJdbc<Promotion> implements PromotionDao {

    public PromotionDaoJdbc(DataSource dataSource) {
        super(dataSource, "promotions", List.of(
                "name", "description", "active_since", "active_until"));
    }

    @Override
    void mapObjectToRow(Promotion promotion, PreparedStatement row) {
        try {
            int c = 0;
            row.setString(++c, promotion.getName());
            row.setString(++c, promotion.getDescription());
            Instant since = promotion.getActiveSince();
            row.setTimestamp(++c, since != null ? Timestamp.from(since)
                                                : null);
            Instant until = promotion.getActiveUntil();
            row.setTimestamp(++c, until != null ? Timestamp.from(until)
                                                : null);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    Promotion mapRowToObject(ResultSet row) {
        try {
            Promotion promotion = new Promotion();
            promotion.setId(row.getLong("id"));
            promotion.setName(row.getString("name"));
            promotion.setDescription(row.getString("description"));
            Timestamp since = row.getTimestamp("active_since");
            if (since != null)  promotion.setActiveSince(since.toInstant());
            Timestamp until = row.getTimestamp("active_until");
            if (until != null) promotion.setActiveUntil(until.toInstant());
            return promotion;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    Promotion objectWithId(Promotion promotion, long id) {
        promotion.setId(id);
        return promotion;
    }

    private final String sqlSelectWherePeriodContains = sqlSelect
            + " WHERE (active_since IS NOT DISTINCT FROM null OR active_since <= ?)"
            + " AND (active_until IS NOT DISTINCT FROM null OR active_until >= ?)";   //TODO check if this works with MySQL

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Promotion> findByActivePeriodContains(Instant instant) {
        return (Iterable<Promotion>) findMany(
                sqlSelectWherePeriodContains,
                fillWithTwoInstants(instant, instant),
                this::mapRowsToObjects);
    }

    private Consumer<PreparedStatement> fillWithTwoInstants(Instant i1, Instant i2) {
        return statement -> {
            try {
                int c = 0;
                statement.setTimestamp(++c, Timestamp.from(i1));
                statement.setTimestamp(++c, Timestamp.from(i2));
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        };
    }
}
