package by.aab.isp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.aab.isp.repository.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("FROM Subscription s WHERE s.customer.id = :customerId AND s.activeSince <= :instant AND s.activeUntil >= :instant")
    List<Subscription> findByCustomerIdAndActivePeriodContains(@Param("customerId") long customerId, @Param("instant") LocalDateTime instant);

    List<Subscription> findByCustomerId(long customerId, Sort sort);

}
