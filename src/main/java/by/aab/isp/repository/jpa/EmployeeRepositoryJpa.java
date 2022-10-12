package by.aab.isp.repository.jpa;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import by.aab.isp.dao.OrderOffsetLimit.Order;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Employee.Role;
import by.aab.isp.repository.EmployeeRepository;

@Repository
@Transactional
public class EmployeeRepositoryJpa extends AbstractRepositoryJpa<Employee> implements EmployeeRepository {

    public EmployeeRepositoryJpa() {
        super(Employee.class);
    }

    private final String qlCountWhereRoleAndActive = qlCount + " WHERE role = :role AND active =";

    @Override
    public long countByRoleAndActive(Role role, boolean active) {
        String ql = qlCountWhereRoleAndActive + active;
        TypedQuery<Long> query = entityManager.createQuery(ql, Long.class);
        query.setParameter("role", role);
        return query.getSingleResult();
    }

    private final String qlCountWhereNotIdAndRoleAndActive = qlCount + " WHERE user_id != :user_id AND role = :role AND active = :active";

    @Override
    public long countByNotIdAndRoleAndActive(long id, Role role, boolean active) {
        TypedQuery<Long> query = entityManager.createQuery(qlCountWhereNotIdAndRoleAndActive, Long.class);
        query.setParameter("user_id", id);
        query.setParameter("role", role);
        query.setParameter("active", active);
        return query.getSingleResult();
    }

    @Override
    protected String mapNullsOrder(Order order) {
        return "";
    }

}
