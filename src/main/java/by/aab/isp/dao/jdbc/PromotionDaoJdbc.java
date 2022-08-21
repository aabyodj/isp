package by.aab.isp.dao.jdbc;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.PromotionDao;
import by.aab.isp.entity.Promotion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
            LocalDateTime since = promotion.getActiveSince();
            row.setTimestamp(++c, since != null ? Timestamp.valueOf(since)
                                                : null);
            LocalDateTime until = promotion.getActiveUntil();
            row.setTimestamp(++c, until != null ? Timestamp.valueOf(until)
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
            if (since != null) {
                promotion.setActiveSince(since.toLocalDateTime());
            }
            Timestamp until = row.getTimestamp("active_until");
            if (until != null) {
                promotion.setActiveUntil(until.toLocalDateTime());
            }
            return promotion;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    Promotion objectWithId(Promotion promotion, Long id) {
        promotion.setId(id);
        return promotion;
    }

    private final String sqlSelectWherePeriodContainsOrderBySinceReversedThenByUntil = sqlSelect
            + " WHERE (active_since IS NOT DISTINCT FROM null OR active_since <= ?)"
            + " AND (active_until IS NOT DISTINCT FROM null OR active_until >= ?)"   //TODO check if this works with MySQL
            + " ORDER BY active_since DESC NULLS FIRST,"
            + " active_until ASC NULLS LAST";

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Promotion> findByActivePeriodContainsOrderBySinceReversedThenByUntil(
            LocalDateTime instant, long skip, int limit) {
        return (Iterable<Promotion>) findMany(
                sqlSelectWherePeriodContainsOrderBySinceReversedThenByUntil + " LIMIT " + limit + " OFFSET " + skip,
                fillWithTwoInstants(instant, instant),
                this::mapRowsToObjects);
    }

    private Consumer<PreparedStatement> fillWithTwoInstants(LocalDateTime i1, LocalDateTime i2) {
        return statement -> {
            try {
                int c = 0;
                statement.setTimestamp(++c, Timestamp.valueOf(i1));
                statement.setTimestamp(++c, Timestamp.valueOf(i2));
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        };
    }
}
