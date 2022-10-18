package by.aab.isp.repository.jpa;

import org.springframework.stereotype.Repository;

import by.aab.isp.entity.Customer;
import by.aab.isp.repository.CustomerRepository;

@Repository
public class CustomerRepositoryJpa extends AbstractRepositoryJpa<Customer> implements CustomerRepository {
}
