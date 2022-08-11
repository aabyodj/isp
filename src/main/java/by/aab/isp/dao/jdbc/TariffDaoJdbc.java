package by.aab.isp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.TariffDao;
import by.aab.isp.entity.Tariff;

public class TariffDaoJdbc extends AbstractRepositoryJdbc<Tariff> implements TariffDao {

    public TariffDaoJdbc(DataSource dataSource) {
        super(dataSource, "tariff", List.of("name", "description", "price"));
    }

    @Override
    void mapObjectToRow(Tariff tariff, PreparedStatement row) {
        try {
            int c = 0;
            row.setString(++c, tariff.getName());
            row.setString(++c, tariff.getDescription());
            row.setBigDecimal(++c, tariff.getPrice());
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
            tariff.setPrice(row.getBigDecimal("price"));
            return tariff;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    Tariff objectWithId(Tariff tariff, long id) {
        tariff.setId(id);
        return tariff;
    }

}
