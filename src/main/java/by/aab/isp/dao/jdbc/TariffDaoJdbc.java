package by.aab.isp.dao.jdbc;

import java.sql.*;
import java.util.List;
import java.util.Map;

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

    public TariffDaoJdbc(DataSourceJdbc dataSource) {
        super(dataSource, "tariff", List.of(
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
    void mapObjectToRow(Tariff tariff, PreparedStatement row) {
        try {
            int c = 0;
            row.setString(++c, tariff.getName());
            row.setString(++c, tariff.getDescription());
            Integer bandwidth = tariff.getBandwidth();
            if (bandwidth != null) {
                row.setInt(++c, bandwidth);
            } else {
                row.setNull(++c, Types.INTEGER);
            }
            Long traffic = tariff.getIncludedTraffic();
            if (traffic != null) {
                row.setLong(++c, traffic);
            } else {
                row.setNull(++c, Types.BIGINT);
            }
            row.setBigDecimal(++c, tariff.getPrice());
            row.setBoolean(++c, tariff.isActive());
        } catch (SQLException e) {
            throw new DaoException(e);
        }        
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

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Tariff> findByActive(boolean active) {
        return (Iterable<Tariff>) findMany(sqlSelectWhereActive + active, this::mapRowsToObjects);
    }
}
