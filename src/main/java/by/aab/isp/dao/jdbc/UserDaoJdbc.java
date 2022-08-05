package by.aab.isp.dao.jdbc;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc extends AbstractRepositoryJdbc<User> implements UserDao {
    public UserDaoJdbc(DataSource dataSource) {
        super(dataSource, "users", List.of(
                new SqlParameter("email", "varchar(25) UNIQUE NOT NULL"),
                new SqlParameter("role_id", "INTEGER NOT NULL")
        ));
    }

    @Override
    void mapObjectToRow(User user, PreparedStatement row) {
        try {
            int c = 0;
            row.setString(++c, user.getEmail());
            row.setInt(++c, user.getRole().ordinal());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    User mapRowToObject(ResultSet row) {
        try {
            return new User(
                    row.getLong("id"),
                    row.getString("email"),
                    User.Role.values()[row.getInt("role_id")]
            );
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    User objectWithId(User user, long id) {
        user.setId(id);
        return user;
    }
}
