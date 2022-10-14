package by.aab.isp.repository;

import java.time.LocalDateTime;
import java.util.List;

import by.aab.isp.entity.Subscription;

public interface SubscriptionRepository extends CrudRepository<Subscription> {

    List<Subscription> findByCustomerIdAndActivePeriodContains(long customerId, LocalDateTime instant);
    List<Subscription> findByCustomerId(long customerId, OrderOffsetLimit orderOffsetLimit);

}
