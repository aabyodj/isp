package by.aab.isp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import by.aab.isp.repository.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
