package by.aab.isp.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.OrderOffsetLimit;
import by.aab.isp.dao.PromotionDao;
import by.aab.isp.entity.Promotion;

@Repository("promotionDao")
public class PromotionDaoJdbc extends AbstractRepositoryJdbc<Promotion> implements PromotionDao {

    private static final Map<String, String> FIELD_NAMES_MAP = Map.of(
            "name", "name",
            "description", "description",
            "activeSince", "active_since",
            "activeUntil", "active_until"
    );

    public PromotionDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "promotions", List.of(
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
    Map<String, ?> entityToMap(Promotion promotion) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", promotion.getName());
        result.put("description", promotion.getDescription());
        result.put("active_since", promotion.getActiveSince());
        result.put("active_until", promotion.getActiveUntil());
        return result;
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
            + " WHERE (" + quote("active_since") + " IS NOT DISTINCT FROM null OR " + quote("active_since") + " <= :active_since)"
            + " AND (" + quote("active_until") + " IS NOT DISTINCT FROM null OR " + quote("active_until") + " >= :active_until)";   //TODO check if this works with MySQL


    @Override
    public Iterable<Promotion> findByActivePeriodContains(LocalDateTime instant, OrderOffsetLimit orderOffsetLimit) {
        return jdbcTemplate.query(
                sqlSelectWherePeriodContains + formatOrderOffsetLimit(orderOffsetLimit),
                Map.of(
                        "active_since", instant, 
                        "active_until", instant),
                this::mapRowsToObjects);
    }
}
