package by.aab.isp.dao.jdbc;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.OrderOffsetLimit;
import by.aab.isp.dao.TariffDao;
import by.aab.isp.entity.Tariff;

@Repository("tariffDao")
public class TariffDaoJdbc extends AbstractRepositoryJdbc<Tariff> implements TariffDao {

    private static final Map<String, String> FIELD_NAMES_MAP = Map.of(
            "name", "name",
            "description", "description",
            "bandwidth", "bandwidth",
            "includedTraffic", "included_traffic",
            "price", "price",
            "active", "active"
    );

    public TariffDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "tariff", List.of(
                "name", "description", "bandwidth", "included_traffic", "price", "active"));
    }

    @Override
    String mapFieldName(String fieldName) {
        return FIELD_NAMES_MAP.get(fieldName);
    }

    @Override
    String mapNullsOrder(OrderOffsetLimit.Order order) {
        if ("bandwidth".equals(order.getFieldName()) || "includedTraffic".equals(order.getFieldName())) {
            return " NULLS " + (order.isAscending() ? "LAST" : "FIRST");
        }
        return "";
    }

    @Override
    Map<String, ?> entityToMap(Tariff tariff) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", tariff.getName());
        result.put("description", tariff.getDescription());
        result.put("bandwidth", tariff.getBandwidth());
        result.put("included_traffic", tariff.getIncludedTraffic());
        result.put("price", tariff.getPrice());
        result.put("active", tariff.isActive());
        return result;
    }

    @Override
    Tariff mapRowToObject(ResultSet row) {
        try {
            Tariff tariff = new Tariff();
            tariff.setId(row.getLong("id"));
            tariff.setName(row.getString("name"));
            tariff.setDescription(row.getString("description"));
            tariff.setBandwidth(nullableInt(row, "bandwidth"));
            tariff.setIncludedTraffic(nullableLong(row, "included_traffic"));
            tariff.setPrice(row.getBigDecimal("price"));
            tariff.setActive(row.getBoolean("active"));
            return tariff;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    Tariff objectWithId(Tariff tariff, Long id) {
        tariff.setId(id);
        return tariff;
    }

    private final String sqlSelectWhereActive = sqlSelect
            + " WHERE " + quote("active") + "=";

    @Override
    public Iterable<Tariff> findByActive(boolean active) {
        return jdbcTemplate.query(sqlSelectWhereActive + active, this::mapRowsToObjects);
    }
}
