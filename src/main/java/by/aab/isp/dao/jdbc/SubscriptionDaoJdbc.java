package by.aab.isp.dao.jdbc;

import by.aab.isp.dao.*;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;
import by.aab.isp.entity.Tariff;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SubscriptionDaoJdbc extends AbstractRepositoryJdbc<Subscription> implements SubscriptionDao {

    private final UserDao userDao;
    private final TariffDao tariffDao;

    public SubscriptionDaoJdbc(DataSource dataSource, UserDao userDao, TariffDao tariffDao) {
        super(dataSource, "subscriptions", List.of(
                "customer_id", "tariff_id", "price",
                "traffic_consumed", "traffic_per_period", "active_since", "active_until"
        ));
        this.userDao = userDao;
        this.tariffDao = tariffDao;
    }

    @Override
    void mapObjectToRow(Subscription subscription, PreparedStatement row) {
        try {
            int c = 0;
            row.setLong(++c, subscription.getCustomer().getId());
            row.setLong(++c, subscription.getTariff().getId());
            row.setBigDecimal(++c, subscription.getPrice());
            row.setLong(++c, subscription.getTrafficConsumed());
            Long totalTraffic = subscription.getTrafficPerPeriod();
            if (totalTraffic != null) {
                row.setLong(++c, totalTraffic);
            } else {
                row.setNull(++c, Types.BIGINT);
            }
            Instant activeSince = subscription.getActiveSince();
            row.setTimestamp(++c, activeSince != null ? Timestamp.from(activeSince)
                                                      : null);
            Instant activeUntil = subscription.getActiveUntil();
            row.setTimestamp(++c, activeUntil != null ? Timestamp.from(activeUntil)
                                                      : null);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    Subscription mapRowToObject(ResultSet row) {
        try {
            Subscription subscription = new Subscription();
            subscription.setId(row.getLong("id"));
            Customer customer = userDao.findCustomerById(row.getLong("customer_id")).orElseThrow();
            subscription.setCustomer(customer);
            Tariff tariff = tariffDao.findById(row.getLong("tariff_id")).orElseThrow();
            subscription.setTariff(tariff);
            subscription.setPrice(row.getBigDecimal("price"));
            subscription.setTrafficConsumed(row.getLong("traffic_consumed"));
            subscription.setTrafficPerPeriod(nullableLong(row, "traffic_per_period"));
            Timestamp activeSince = row.getTimestamp("active_since");
            subscription.setActiveSince(activeSince != null ? activeSince.toInstant()
                                                            : null);
            Timestamp activeUntil = row.getTimestamp("active_until");
            subscription.setActiveUntil(activeUntil != null ? activeUntil.toInstant()
                                                            : null);
            return subscription;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    Collection<Subscription> mapRowsToObjects(ResultSet resultSet) {
        try {
            List<Row> rows = new LinkedList<>();
            Map<Long, Customer> customers = new HashMap<>();
            Map<Long, Tariff> tariffs = new HashMap<>();
            while (resultSet.next()) {
                Row row = new Row(
                        resultSet.getLong("id"),
                        resultSet.getLong("customer_id"),
                        resultSet.getLong("tariff_id"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getLong("traffic_consumed"),
                        nullableLong(resultSet, "traffic_per_period"),
                        resultSet.getTimestamp("active_since"),
                        resultSet.getTimestamp("active_until")
                );
                rows.add(row);
                customers.put(row.getCustomer_id(), null);
                tariffs.put(row.getTariff_id(), null);
            }
            customers.replaceAll((id, customer) -> userDao.findCustomerById(id).orElseThrow());
            tariffs.replaceAll((id, tariff) -> tariffDao.findById(id).orElseThrow());
            return rows.stream()
                    .map(row -> row.toSubscription(customers, tariffs))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    Subscription objectWithId(Subscription subscription, long id) {
        subscription.setId(id);
        return subscription;
    }

    private final String sqlSelectWhereCustomerAndPeriodContains = sqlSelect
            + " WHERE customer_id = ?"
            + " AND (active_since IS NOT DISTINCT FROM null OR active_since <= ?)"
            + " AND (active_until IS NOT DISTINCT FROM null OR active_until >= ?)";   //TODO ensure this works with MySQL

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Subscription> findByCustomerIdAndActivePeriodContains(long customerId, Instant instant) {
        return (Iterable<Subscription>) findMany(
                sqlSelectWhereCustomerAndPeriodContains,
                fillWithCustomerIdAndTwoInstants(customerId, instant, instant),
                this::mapRowsToObjects);
    }

    private final String sqlSelectWhereCustomerId = sqlSelect + " WHERE customer_id=";

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Subscription> findByCustomerId(long customerId) {
        return (Iterable<Subscription>) findMany(sqlSelectWhereCustomerId + customerId, this::mapRowsToObjects);
    }

    private Consumer<PreparedStatement> fillWithCustomerIdAndTwoInstants(long customerId, Instant instant1, Instant instant2) {
        return statement -> {
            try {
                int c = 0;
                statement.setLong(++c, customerId);
                statement.setTimestamp(++c, Timestamp.from(instant1));
                statement.setTimestamp(++c, Timestamp.from(instant2));
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        };
    }

    @Data
    private static class Row {
        private final long id;
        private final long customer_id;
        private final long tariff_id;
        private final BigDecimal price;
        private final long trafficConsumed;
        private final Long trafficPerPeriod;
        private final Timestamp activeSince;
        private final Timestamp activeUntil;

        Subscription toSubscription(Map<Long, Customer> customers, Map<Long, Tariff> tariffs) {
            Subscription subscription = new Subscription();
            subscription.setId(id);
            subscription.setCustomer(customers.get(customer_id));
            subscription.setTariff(tariffs.get(tariff_id));
            subscription.setPrice(price);
            subscription.setTrafficConsumed(trafficConsumed);
            subscription.setTrafficPerPeriod(trafficPerPeriod);
            subscription.setActiveSince(activeSince != null ? activeSince.toInstant()
                                                            : null);
            subscription.setActiveUntil(activeUntil != null ? activeUntil.toInstant()
                                                            : null);
            return subscription;
        }
    }
}
