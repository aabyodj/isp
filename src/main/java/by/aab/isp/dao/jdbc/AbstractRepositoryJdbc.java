package by.aab.isp.dao.jdbc;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import by.aab.isp.dao.CrudRepository;
import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.OrderOffsetLimit;
import by.aab.isp.entity.Entity;

abstract class AbstractRepositoryJdbc<T extends Entity> implements CrudRepository<T> {

    static final char QUOTE_CHAR = '"';		//TODO: get rid of this
    
    final NamedParameterJdbcTemplate jdbcTemplate;
    final String tableName;
    final String quotedTableName;
    final String doubledQuoteChar;
    final Pattern quoteCharPattern;

    final String sqlInsert;
    final String sqlCount;
    final String sqlSelect;
    final String sqlSelectWhereId;
    final String sqlUpdate;
    final String sqlUpdateWhereId;
    
    AbstractRepositoryJdbc(NamedParameterJdbcTemplate jdbcTemplate, String tableName, List<String> fields) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
        this.doubledQuoteChar = Character.toString(QUOTE_CHAR) + QUOTE_CHAR;
        this.quoteCharPattern = Pattern.compile(Pattern.quote(Character.toString(QUOTE_CHAR)));
        this.quotedTableName = quote(tableName);
        sqlInsert = "INSERT INTO " + quotedTableName
                + fields.stream()
                        .map(this::quote)
                        .reduce(new StringJoiner(",", "(", ")"),
                                StringJoiner::add,
                                StringJoiner::merge)
                + " VALUES"
                + fields.stream()
                        .map(field -> ":" + field)
                        .reduce(new StringJoiner(",", "(", ")"),
                                StringJoiner::add,
                                StringJoiner::merge);
        sqlCount = "SELECT count(*) FROM " + quotedTableName;
        sqlSelect = "SELECT * FROM " + quotedTableName;
        sqlSelectWhereId = sqlSelect + " WHERE " + quote("id") + "=";
        sqlUpdate = "UPDATE " + quotedTableName + " SET " + fields.stream()
                .map(field -> quote(field) + " = :" + field)
                .reduce(new StringJoiner(", "),
                        StringJoiner::add,
                        StringJoiner::merge);
        sqlUpdateWhereId = sqlUpdate + " WHERE " + quote("id") + "=";
    }

    @Override
    public T save(T entity) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, ?> paramMap = entityToMap(entity);
        jdbcTemplate.update(sqlInsert, new MapSqlParameterSource(paramMap), keyHolder);
        Long id = ((Number) keyHolder.getKeys().get("id")).longValue();
        return objectWithId(entity, id);
    }

    long count(String sql) {
        return jdbcTemplate.query(sql, resultSet -> {
            resultSet.next();
            return resultSet.getLong(1);
        });
    }

    @Override
    public long count() {
        return count(sqlCount);
    }

    @Override
    public Iterable<T> findAll() {
        return jdbcTemplate.query(sqlSelect, this::mapRowsToObjects);
    }

    @Override
    public Iterable<T> findAll(OrderOffsetLimit orderOffsetLimit) {
        return jdbcTemplate.query(sqlSelect + formatOrderOffsetLimit(orderOffsetLimit), this::mapRowsToObjects);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<T> findById(long id) {
        return (Optional<T>) findOne(sqlSelectWhereId + id, this::mapRowToObject);
    }

	@Override
	public void update(T entity) {
		if (jdbcTemplate.update(sqlUpdateWhereId + entity.getId(), entityToMap(entity)) < 1) {
			throw new DaoException("Could not update " + entity.getClass().getName());
		}
	}
    
	Optional<? extends T> findOne(String sql, Function<ResultSet, T> mapper) {
	    return jdbcTemplate.query(sql, resultSet -> resultSet.next() ? Optional.of(mapper.apply(resultSet)) 
	                                                                 : Optional.empty());
	}

	Optional<? extends T> findOne(String sql, Map<String, ?> paramMap, Function<ResultSet, T> mapper) {
	    return jdbcTemplate.query(sql, paramMap, resultSet -> resultSet.next() ? Optional.of(mapper.apply(resultSet)) 
	                                                                           : Optional.empty());
	}

    String quote(String identifier) {
        identifier = quoteCharPattern.matcher(identifier).replaceAll(doubledQuoteChar);
        return QUOTE_CHAR + identifier + QUOTE_CHAR;
    }

    static Integer nullableInt(ResultSet resultSet, String name) throws SQLException {
        Integer result = resultSet.getInt(name);
        if (resultSet.wasNull()) {
            result = null;
        }
        return result;
    }

    static Long nullableLong(ResultSet resultSet, String name) throws SQLException {
        Long result = resultSet.getLong(name);
        if (resultSet.wasNull()) {
            result = null;
        }
        return result;
    }

    String formatOrder(OrderOffsetLimit.Order order) {
        String dbField = mapFieldName(order.getFieldName());
        if (null == dbField) {
            throw new DaoException("There is no field '" + order.getFieldName() + "'");
        }
        dbField = quote(dbField);
        return dbField
                + (order.isAscending() ? " ASC" : " DESC")
                + mapNullsOrder(order);
    }

    String formatOrderList(List<OrderOffsetLimit.Order> orderList) {
        if (null == orderList || orderList.isEmpty()) {
            return "";
        }
        return " ORDER BY " + orderList.stream()
                .map(this::formatOrder)
                .reduce(new StringJoiner(","),
                        StringJoiner::add,
                        StringJoiner::merge);
    }

    String formatOrderOffsetLimit(OrderOffsetLimit orderOffsetLimit) {
        if (null == orderOffsetLimit) {
            return "";
        }
        Long offset = orderOffsetLimit.getOffset();
        Integer limit = orderOffsetLimit.getLimit();
        String result = formatOrderList(orderOffsetLimit.getOrderList());
        if (limit != null) {
            result += " LIMIT " + limit;
        }
        if (offset != null) {
            result += " OFFSET " + offset;
        }
        return result;
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

    abstract String mapFieldName(String fieldName);

    abstract String mapNullsOrder(OrderOffsetLimit.Order order);

    abstract Map<String, ?> entityToMap(T entity);
    
    abstract T mapRowToObject(ResultSet row);
    
    abstract T objectWithId(T object, Long id);

}
