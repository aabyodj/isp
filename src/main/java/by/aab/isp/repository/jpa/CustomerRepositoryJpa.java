package by.aab.isp.repository.jpa;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import by.aab.isp.entity.Customer;
import by.aab.isp.repository.CustomerRepository;
import by.aab.isp.repository.OrderOffsetLimit.Order;

@Repository
@Transactional
public class CustomerRepositoryJpa extends AbstractRepositoryJpa<Customer> implements CustomerRepository {

    @Override
    protected String mapNullsOrder(Order order) {   //TODO: remove this
        return "";
    }

}
