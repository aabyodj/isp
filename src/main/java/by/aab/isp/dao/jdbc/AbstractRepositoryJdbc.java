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
import java.util.function.Consumer;
import java.util.function.Function;

import by.aab.isp.dao.CrudRepository;
import by.aab.isp.dao.DaoException;
import by.aab.isp.entity.Entity;

abstract class AbstractRepositoryJdbc<T extends Entity> implements CrudRepository<T> {
    
    final DataSource dataSource;
    final String tableName;

    final String sqlInsert;
    final String sqlCount;
    final String sqlSelect;
    final String sqlSelectWhereId;
    final String sqlUpdate;
    final String sqlUpdateWhereId;
    
    AbstractRepositoryJdbc(DataSource dataSource, String tableName, List<String> fields) {
        this.dataSource = dataSource;
        this.tableName = tableName;
        sqlInsert = "INSERT INTO " + tableName
                + fields.stream()
                        .reduce(new StringJoiner(",", "(", ")"),
                                StringJoiner::add,
                                StringJoiner::merge)
                + "VALUES"
                + fields.stream()
                        .map(field -> "?")
                        .reduce(new StringJoiner(",", "(", ")"),
                                StringJoiner::add,
                                StringJoiner::merge);
        sqlCount = "SELECT count(*) FROM " + tableName;
        sqlSelect = "SELECT * FROM " + tableName;
        sqlSelectWhereId = sqlSelect + " WHERE id=";
        sqlUpdate = "UPDATE " + tableName + " SET " + fields.stream()
                .map(field -> field + " = ?")
                .reduce(new StringJoiner(", "),
                        StringJoiner::add,
                        StringJoiner::merge);
        sqlUpdateWhereId = sqlUpdate + " WHERE id=";
    }

    @Override
    public void init() {
    }

    @Override
    public T save(T entity) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            mapObjectToRow(entity, statement);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return objectWithId(entity, resultSet.getLong(1));
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    long count(String sql) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public long count() {
        return count(sqlCount);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<T> findAll() {
        return (Iterable<T>) findMany(sqlSelect, this::mapRowsToObjects);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<T> findById(long id) {
        return (Optional<T>) findOne(sqlSelectWhereId + id, this::mapRowToObject);
    }

    @Override
    public void update(T entity) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sqlUpdateWhereId + entity.getId());
            mapObjectToRow(entity, statement);
            if (statement.executeUpdate() < 1) {
                throw new DaoException("Could not update " + entity.getClass().getName());
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    Iterable<? extends T> findMany(String sql, Consumer<PreparedStatement> filler, Function<ResultSet, Iterable<? extends T>> mapper) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            filler.accept(statement);
            ResultSet resultSet = statement.executeQuery();
            return mapper.apply(resultSet);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    Iterable<? extends T> findMany(String sql, Function<ResultSet, Iterable<? extends T>> mapper) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return mapper.apply(resultSet);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    
    Optional<? extends T> findOne(String sql, Function<ResultSet, T> mapper) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next() ? Optional.of(mapper.apply(resultSet)) 
                                    : Optional.empty();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    Optional<? extends T> findOne(String sql, Consumer<PreparedStatement> filler, Function<ResultSet, T> mapper) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            filler.accept(statement);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? Optional.of(mapper.apply(resultSet))
                                    : Optional.empty();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    
    int executeUpdate(String sql, Consumer<PreparedStatement> filler) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            filler.accept(statement);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
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

}
