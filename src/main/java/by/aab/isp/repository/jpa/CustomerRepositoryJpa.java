package by.aab.isp.repository.jpa;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import by.aab.isp.dao.OrderOffsetLimit.Order;
import by.aab.isp.entity.Customer;
import by.aab.isp.repository.CustomerRepository;

@Repository
@Transactional
public class CustomerRepositoryJpa extends AbstractRepositoryJpa<Customer> implements CustomerRepository {

    public CustomerRepositoryJpa() {
        super(Customer.class);
    }

    @Override
    protected String mapNullsOrder(Order order) {
        if ("payoffDate".equals(order.getFieldName())) {
            return " NULLS " + (order.isAscending() ? "LAST" : "FIRST");
        }
        return "";
    }

}
