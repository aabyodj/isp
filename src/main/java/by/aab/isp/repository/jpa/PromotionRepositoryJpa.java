package by.aab.isp.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import by.aab.isp.dao.OrderOffsetLimit;
import by.aab.isp.dao.OrderOffsetLimit.Order;
import by.aab.isp.entity.Promotion;
import by.aab.isp.repository.PromotionRepository;

@Transactional
@Repository
public class PromotionRepositoryJpa extends AbstractRepositoryJpa<Promotion> implements PromotionRepository {

    public PromotionRepositoryJpa() {
        super(Promotion.class);
    }

    private final String qlSelectWherePeriodContains = qlSelectAll
            + " WHERE (activeSince IS null OR activeSince <= :active_since)"
            + " AND (activeUntil IS null OR activeUntil >= :active_until)";

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
    protected String mapNullsOrder(Order order) {
        if ("activeSince".equals(order.getFieldName())) {
            return " NULLS " + (order.isAscending() ? "FIRST" : "LAST");
        }
        if ("activeUntil".equals(order.getFieldName())) {
            return " NULLS " + (order.isAscending() ? "LAST" : "FIRST");
        }
        return "";
    }

}
