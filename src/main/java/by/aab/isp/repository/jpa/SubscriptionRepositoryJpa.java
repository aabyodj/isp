package by.aab.isp.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import by.aab.isp.entity.Subscription;
import by.aab.isp.repository.OrderOffsetLimit;
import by.aab.isp.repository.SubscriptionRepository;

@Transactional
@Repository
public class SubscriptionRepositoryJpa extends AbstractRepositoryJpa<Subscription> implements SubscriptionRepository {

    private final String qlSelectWhereCustomerAndPeriodContains = qlSelectAll
            + " WHERE customer_id = :customer_id"
            + " AND activeSince <= :active_since AND activeUntil >= :active_until";

    @Override
    public List<Subscription> findByCustomerIdAndActivePeriodContains(long customerId, LocalDateTime instant) {
        TypedQuery<Subscription> query = entityManager.createQuery(qlSelectWhereCustomerAndPeriodContains, Subscription.class);
        query.setParameter("customer_id", customerId);
        query.setParameter("active_since", instant);
        query.setParameter("active_until", instant);
        return query.getResultList();        
    }

    private final String qlSelectWhereCustomerId = qlSelectAll + " WHERE customer_id=";
    
    @Override
    public List<Subscription> findByCustomerId(long customerId, OrderOffsetLimit orderOffsetLimit) {
        String ql = qlSelectWhereCustomerId + customerId + formatOrderList(orderOffsetLimit.getOrderList());
        TypedQuery<Subscription> query = entityManager.createQuery(ql, Subscription.class);
        applyOffsetLimit(query, orderOffsetLimit.getOffset(), orderOffsetLimit.getLimit());
        return query.getResultList();
    }

}
