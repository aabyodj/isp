package by.aab.isp.dao.jdbc;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.OrderOffsetLimit;
import by.aab.isp.dao.PromotionDao;
import by.aab.isp.entity.Promotion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.stereotype.Repository;

@Repository("promotionDao")
public class PromotionDaoJdbc extends AbstractRepositoryJdbc<Promotion> implements PromotionDao {

    private static final Map<String, String> FIELD_NAMES_MAP = Map.of(
            "name", "name",
            "description", "description",
            "activeSince", "active_since",
            "activeUntil", "active_until"
    );

    public PromotionDaoJdbc(DataSourceJdbc dataSource) {
        super(dataSource, "promotions", List.of(
                "name", "description", "active_since", "active_until"));
    }

    @Override
    String mapFieldName(String fieldName) {
        return FIELD_NAMES_MAP.get(fieldName);
    }

    @Override
    String mapNullsOrder(OrderOffsetLimit.Order order) {
        if ("activeSince".equals(order.getFieldName())) {
            return " NULLS " + (order.isAscending() ? "FIRST" : "LAST");
        }
        if ("activeUntil".equals(order.getFieldName())) {
            return " NULLS " + (order.isAscending() ? "LAST" : "FIRST");
        }
        return "";
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

    private final String sqlSelectWherePeriodContains = sqlSelect
            + " WHERE (" + quote("active_since") + " IS NOT DISTINCT FROM null OR " + quote("active_since") + " <= ?)"
            + " AND (" + quote("active_until") + " IS NOT DISTINCT FROM null OR " + quote("active_until") + " >= ?)";   //TODO check if this works with MySQL


    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Promotion> findByActivePeriodContains(LocalDateTime instant, OrderOffsetLimit orderOffsetLimit) {
        return (Iterable<Promotion>) findMany(
                sqlSelectWherePeriodContains + formatOrderOffsetLimit(orderOffsetLimit),
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
