package by.aab.isp.dao.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

import by.aab.isp.dao.CrudRepository;
import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.DataSource;

abstract class AbstractRepositoryJdbc<T> implements CrudRepository<T> {
    
    final DataSource dataSource;
    final String tableName;
    
    final String sqlCreateTable;
    final String sqlInsert;
    final String sqlSelect;
    final String sqlSelectWhereId;
    
    AbstractRepositoryJdbc(DataSource dataSource, String tableName, List<SqlParameter> fields) {
        this.dataSource = dataSource;
        this.tableName = tableName;
        sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + tableName
                + fields.stream()
                        .map(field -> field.getName() + " " + field.getType())
                        .reduce(new StringJoiner(",", "(", ");"), StringJoiner::add, StringJoiner::merge);
        sqlInsert = "INSERT INTO " + tableName
                + fields.stream()
                    .map(SqlParameter::getName)
                    .reduce(new StringJoiner(",", "(", ")"),
                            StringJoiner::add,
                            StringJoiner::merge)
                + "VALUES"
                + fields.stream()
                    .map(field -> "?")
                    .reduce(new StringJoiner(",", "(", ")"),
                            StringJoiner::add,
                            StringJoiner::merge);
        sqlSelect = "SELECT * FROM " + tableName;
        sqlSelectWhereId = sqlSelect + " WHERE id=";
    }
    
    public void init() {
        executeUpdate(sqlCreateTable);
    }

    @Override
    public Collection<T> findAll() {
        return findMany(sqlSelect, this::mapRowsToObjects);
    }

    @Override
    public Optional<T> findById(long id) {
        return findOne(sqlSelectWhereId + id, this::mapRowToObject);
    }    
    
    @Override
    public T save(T object) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            mapObjectToRow(object, statement);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            T result = objectWithId(object, resultSet.getLong(1));
            return result;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeConnection(connection, statement);
        }
    }

    Collection<T> findMany(String sql, Function<ResultSet, Collection<T>> mapper) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return mapper.apply(resultSet);
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeConnection(connection, statement);
        }
    }
    
    Optional<T> findOne(String sql, Function<ResultSet, T> mapper) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next() ? Optional.of(mapper.apply(resultSet)) 
                                    : Optional.empty();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeConnection(connection, statement);
        }
    }
    
    int executeUpdate(String sql) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeConnection(connection, statement);
        }
    }
    
    static void closeConnection(Connection connection, Statement statement) {
        try {
            if (statement != null && !statement.isClosed()) statement.close();
        } catch (SQLException ignore) {
        }
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException ignore) {
        }
    }
    
    Collection<T> mapRowsToObjects(ResultSet rows) {
        try {
            Collection<T> result = new LinkedList<>();
            while (rows.next()) {
                result.add(mapRowToObject(rows));
            }
            return result;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    
    abstract void mapObjectToRow(T object, PreparedStatement row);
    
    abstract T mapRowToObject(ResultSet row);
    
    abstract T objectWithId(T object, long id);
    
    static class SqlParameter {
        
        final String name;
        final String type;
        
        public SqlParameter(String name, String type) {
            super();
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
        
    }
}
