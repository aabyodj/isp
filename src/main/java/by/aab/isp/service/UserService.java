package by.aab.isp.service;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;

public interface UserService {

    Iterable<Customer> getAllCustomers();

    Iterable<Employee> getAllEmployees();

    User getById(long id);

    User save(User user);

    User login(String email, String password);
}
