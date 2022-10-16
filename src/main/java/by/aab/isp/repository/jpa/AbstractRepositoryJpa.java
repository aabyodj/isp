package by.aab.isp.repository.jpa;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import by.aab.isp.repository.CrudRepository;
import by.aab.isp.repository.OrderOffsetLimit;

@Transactional
public abstract class AbstractRepositoryJpa<T> implements CrudRepository<T> {
    
    protected final Class<T> clazz;
    protected final String qlCount;
    protected final String qlSelectAll;

    @PersistenceContext
    protected EntityManager entityManager;
    
    @SuppressWarnings("unchecked")
    public AbstractRepositoryJpa() {
        this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        qlSelectAll = "FROM " + clazz.getName();
        qlCount = "SELECT count(*) " + qlSelectAll;
    }

    @Override
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(qlCount, Long.class);
        return query.getSingleResult();
    }

    @Override
    public List<T> findAll() {
        TypedQuery<T> query = entityManager.createQuery(qlSelectAll, clazz);
        return query.getResultList();
    }

    @Override
    public List<T> findAll(OrderOffsetLimit orderOffsetLimit) {
        String ql = qlSelectAll + formatOrderList(orderOffsetLimit.getOrderList());
        TypedQuery<T> query = entityManager.createQuery(ql, clazz);
        applyOffsetLimit(query, orderOffsetLimit.getOffset(), orderOffsetLimit.getLimit());
        return query.getResultList();
    }

    @Override
    public Optional<T> findById(long id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public void update(T entity) {
        if (!entityManager.contains(entity)) {
            entityManager.merge(entity);
        }
    }
    
    protected final String formatOrder(OrderOffsetLimit.Order order) {
        return order.getFieldName()
                + (order.isAscending() ? " ASC" : " DESC")
                + mapNullsOrder(order);
    }

    protected final String formatOrderList(List<OrderOffsetLimit.Order> orderList) {
        if (null == orderList || orderList.isEmpty()) {
            return "";
        }
        return " ORDER BY " + orderList.stream()
                .map(this::formatOrder)
                .reduce(new StringJoiner(","),
                        StringJoiner::add,
                        StringJoiner::merge);
    }

    protected final TypedQuery<T> applyOffsetLimit(TypedQuery<T> query, Long offset, Integer limit) {
        if (offset != null) {
            if (offset > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Offset value exceeds max integer");
            }
            query.setFirstResult(offset.intValue());
        }
        if (limit != null) {
            query.setMaxResults(limit);
        }
        return query;
    }

    protected abstract String mapNullsOrder(OrderOffsetLimit.Order order);
}
