package by.aab.isp.service;

import by.aab.isp.dto.CredentialsDto;
import by.aab.isp.dto.CustomerDto;
import by.aab.isp.dto.EmployeeDto;
import by.aab.isp.dto.UpdateCredentialsDto;
import by.aab.isp.dto.UserDto;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import java.math.BigDecimal;

public interface UserService {

    Iterable<Customer> getAllCustomers(Pagination pagination);

    Iterable<Employee> getAllEmployees(Pagination pagination);

    UserDto getById(long id);

    CustomerDto getCustomerById(Long id);

    EmployeeDto getEmployeeById(Long id);

    UserDto save(UserDto user);

    UserDto login(CredentialsDto credentials);

    void updateCredentials(UpdateCredentialsDto dto);

    void replenishBalance(long customerId, BigDecimal amount);

    void createDefaultAdmin();

    void generateCustomers(int quantity, boolean active);

    void generateEmployees(int quantity, boolean active);
}
