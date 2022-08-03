package by.aab.isp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.DataSource;
import by.aab.isp.dao.TariffDao;
import by.aab.isp.entity.Tariff;

public class TariffDaoJdbc extends AbstractRepositoryJdbc<Tariff> implements TariffDao {

    public TariffDaoJdbc(DataSource dataSource) {
        super(dataSource, "tariff", List.of(new SqlParameter("id", "bigserial NOT NULL"),
                                            new SqlParameter("name", "varchar(15) NOT NULL"),
                                            new SqlParameter("description", "varchar(50) NOT NULL"),
                                            new SqlParameter("price", "decimal(10, 2) NOT NULL")));
        init();
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
            return new Tariff(row.getLong("id"), 
                              row.getString("name"), 
                              row.getString("description"), 
                              row.getBigDecimal("price"));
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
