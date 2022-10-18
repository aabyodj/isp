package by.aab.isp.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import by.aab.isp.entity.Promotion;
import by.aab.isp.repository.OrderOffsetLimit;
import by.aab.isp.repository.PromotionRepository;
import by.aab.isp.repository.OrderOffsetLimit.Order;

@Transactional
@Repository
public class PromotionRepositoryJpa extends AbstractRepositoryJpa<Promotion> implements PromotionRepository {

    private final String qlSelectWherePeriodContains = qlSelectAll
            + " WHERE activeSince <= :active_since AND activeUntil >= :active_until";

    @Override
    public List<Promotion> findByActivePeriodContains(LocalDateTime instant, OrderOffsetLimit orderOffsetLimit) {
        String ql = qlSelectWherePeriodContains + formatOrderList(orderOffsetLimit.getOrderList());
        TypedQuery<Promotion> query = entityManager.createQuery(ql, Promotion.class);
        query.setParameter("active_since", instant);
        query.setParameter("active_until", instant);
        applyOffsetLimit(query, orderOffsetLimit.getOffset(), orderOffsetLimit.getLimit());
        return query.getResultList();
    }

    @Override
    protected String mapNullsOrder(Order order) {   //TODO: remove this
        return "";
    }

}
