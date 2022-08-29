package by.aab.isp.dao;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;

import java.util.Optional;

public interface UserDao extends CrudRepository<User> {

    Iterable<Customer> findAllCustomers(OrderOffsetLimit orderOffsetLimit);

    Iterable<Employee> findAllEmployees(OrderOffsetLimit orderOffsetLimit);

    Optional<Customer> findCustomerById(long id);

    Optional<Employee> findEmployeeById(long id);

    Optional<User> findByEmailAndActive(String email, boolean active);

    long countCustomers();

    long countEmployees();

    long countByRoleAndActive(Employee.Role role, boolean active);

    long countByNotIdAndRoleAndActive(long id, Employee.Role role, boolean active);
}
