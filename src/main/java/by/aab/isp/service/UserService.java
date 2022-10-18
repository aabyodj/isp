package by.aab.isp.service;

import by.aab.isp.dto.user.CredentialsDto;
import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UpdateCredentialsDto;
import by.aab.isp.dto.user.UserDto;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<CustomerDto> getAllCustomers(Pageable pageable);

    Page<EmployeeDto> getAllEmployees(Pageable pageable);

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
