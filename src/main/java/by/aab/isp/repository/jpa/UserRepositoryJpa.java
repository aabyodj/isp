package by.aab.isp.repository.jpa;

import java.util.Optional;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import by.aab.isp.entity.User;
import by.aab.isp.repository.UserRepository;
import by.aab.isp.repository.OrderOffsetLimit.Order;

@Repository
@Transactional
public class UserRepositoryJpa extends AbstractRepositoryJpa<User> implements UserRepository {

    private final String qlSelectWhereEmailAndActive = qlSelectAll + " WHERE email = :email and active =";
    
    @Override
    public Optional<User> findByEmailAndActive(String email, boolean active) {
        String ql = qlSelectWhereEmailAndActive + active;
        TypedQuery<User> query = entityManager.createQuery(ql, User.class);
        query.setParameter("email", email);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    protected String mapNullsOrder(Order order) {
        if ("payoffDate".equals(order.getFieldName())) {
            return " NULLS " + (order.isAscending() ? "LAST" : "FIRST");
        }
        return "";
    }

}
