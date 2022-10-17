package by.aab.isp.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import by.aab.isp.entity.Tariff;
import by.aab.isp.repository.TariffRepository;
import by.aab.isp.repository.OrderOffsetLimit.Order;

@Transactional
@Repository
public class TariffRepositoryJpa extends AbstractRepositoryJpa<Tariff> implements TariffRepository {

    protected final String qlSelectWhereActive = qlSelectAll + " WHERE active=";

    @Override
    public List<Tariff> findByActive(boolean active) {
        TypedQuery<Tariff> query = entityManager.createQuery(qlSelectWhereActive + active, Tariff.class);
        return query.getResultList();
    }

    protected final String qlSelectWhereInactiveForCustomer = qlSelectAll
            + " WHERE active = true AND NOT id IN "
            + "(SELECT DISTINCT tariff.id FROM Subscription WHERE customer_id = :customer_id AND activeSince <= :since AND activeUntil >= :until)";

    @Override
    public List<Tariff> findInactiveForCustomer(long customerId, LocalDateTime moment) {
        TypedQuery<Tariff> query = entityManager.createQuery(qlSelectWhereInactiveForCustomer, Tariff.class);
        query.setParameter("customer_id", customerId);
        query.setParameter("since", moment);
        query.setParameter("until", moment);
        return query.getResultList();
    }

    @Override
    protected String mapNullsOrder(Order order) {   //TODO: remove this
        return "";
    }

}
