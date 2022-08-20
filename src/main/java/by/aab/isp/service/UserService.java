package by.aab.isp.service;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;

import java.math.BigDecimal;

public interface UserService {

    Iterable<Customer> getAllCustomers();

    Iterable<Employee> getAllEmployees();

    User getById(long id);

    Customer getCustomerById(long id);

    Employee getEmployeeById(long id);

    User save(User user, String password);

    User login(String email, String password);

    void updateCredentials(User user, String newEmail, String newPassword, String currentPassword);

    void replenishBalance(Customer customer, BigDecimal amount);

    void createDefaultAdmin();
}
