package by.aab.isp.dao;

import by.aab.isp.entity.Subscription;

import java.time.LocalDateTime;

public interface SubscriptionDao extends CrudRepository<Subscription> {
    Iterable<Subscription> findByCustomerIdAndActivePeriodContains(long customerId, LocalDateTime instant);

    Iterable<Subscription> findByCustomerId(long customerId, OrderOffsetLimit orderOffsetLimit);
}
