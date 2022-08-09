package by.aab.isp.dao.jdbc;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.PromotionDao;
import by.aab.isp.entity.Promotion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PromotionDaoJdbc extends AbstractRepositoryJdbc<Promotion> implements PromotionDao {

    public PromotionDaoJdbc(DataSource dataSource) {
        super(dataSource, "promotions", List.of("name", "description"));
    }

    @Override
    void mapObjectToRow(Promotion promotion, PreparedStatement row) {
        try {
            int c = 0;
            row.setString(++c, promotion.getName());
            row.setString(++c, promotion.getDescription());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    Promotion mapRowToObject(ResultSet row) {
        try {
            return new Promotion(
                    row.getLong("id"),
                    row.getString("name"),
                    row.getString("description")
            );
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    Promotion objectWithId(Promotion promotion, long id) {
        promotion.setId(id);
        return promotion;
    }
}
