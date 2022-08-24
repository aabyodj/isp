package by.aab.isp.dao.jdbc;

import java.sql.*;
import java.util.List;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.TariffDao;
import by.aab.isp.entity.Tariff;

public class TariffDaoJdbc extends AbstractRepositoryJdbc<Tariff> implements TariffDao {

    public TariffDaoJdbc(DataSource dataSource) {
        super(dataSource, "tariff", List.of(
                "name", "description", "bandwidth", "included_traffic", "price", "active"));
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
            + " WHERE active=";

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Tariff> findAll(long skip, int limit) { //TODO: move this to AbstractDaoJdbc
        return (Iterable<Tariff>) findMany(
                sqlSelect + " LIMIT " + limit + " OFFSET " + skip,
                this::mapRowsToObjects);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Tariff> findByActive(boolean active) {
        return (Iterable<Tariff>) findMany(sqlSelectWhereActive + active, this::mapRowsToObjects);
    }
}
