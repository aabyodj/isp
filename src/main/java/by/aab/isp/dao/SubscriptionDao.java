package by.aab.isp.dao;

import by.aab.isp.entity.Subscription;

import java.time.Instant;

public interface SubscriptionDao extends CrudRepository<Subscription> {
    Iterable<Subscription> findByCustomerIdAndActivePeriodContains(long customerId, Instant instant);

    Iterable<Subscription> findByCustomerId(long customerId);
}
