package by.aab.isp.dao.jdbc;

import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDaoJdbc extends AbstractRepositoryJdbc<User> implements UserDao {

    public UserDaoJdbc(DataSource dataSource) {
        super(dataSource, "users", List.of("email", "role_id"));
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
            User user = new User();
            user.setId(row.getLong("id"));
            user.setEmail(row.getString("email"));
            user.setRole(User.Role.values()[row.getInt("role_id")]);
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    User objectWithId(User user, long id) {
        user.setId(id);
        return user;
    }

    private final String sqlSelectWhereEmail = sqlSelect + " WHERE email = ?";

    @Override
    public Optional<User> findByEmail(String email) {
        return findOne(
                sqlSelectWhereEmail,
                statement -> {
                    try {
                        statement.setString(1, email);
                    } catch (SQLException e) {
                        throw new DaoException(e);
                    }
                },
                this::mapRowToObject);
    }

    final String sqlCountWhereRoleId = sqlCount + " WHERE role_id=";

    @Override
    public long countByRoleId(long roleId) {
        return count(sqlCountWhereRoleId + roleId);
    }
}
