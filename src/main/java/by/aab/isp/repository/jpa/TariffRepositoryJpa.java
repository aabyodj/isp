package by.aab.isp.repository.jpa;

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
    
    public TariffRepositoryJpa() {
        super(Tariff.class);
    }

    @Override
    public List<Tariff> findByActive(boolean active) {
        TypedQuery<Tariff> query = entityManager.createQuery(qlSelectWhereActive + active, Tariff.class);
        return query.getResultList();
    }

    @Override
    protected String mapNullsOrder(Order order) {
        if ("bandwidth".equals(order.getFieldName()) || "includedTraffic".equals(order.getFieldName())) {
            return " NULLS " + (order.isAscending() ? "LAST" : "FIRST");
        }
        return "";
    }

}
