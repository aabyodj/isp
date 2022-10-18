package by.aab.isp.service;

import by.aab.isp.dto.user.CredentialsDto;
import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UpdateCredentialsDto;
import by.aab.isp.dto.user.UserDto;
import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    List<CustomerDto> getAllCustomers(Pagination pagination);

    List<EmployeeDto> getAllEmployees(Pagination pagination);

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
