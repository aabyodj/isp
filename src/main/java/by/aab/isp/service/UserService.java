package by.aab.isp.service;

import by.aab.isp.dto.user.CredentialsDto;
import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.CustomerViewDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.EmployeeViewDto;
import by.aab.isp.dto.user.UpdateCredentialsDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.dto.user.UserViewDto;

import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<CustomerViewDto> getAllCustomers(Pageable pageable);

    Page<EmployeeViewDto> getAllEmployees(Pageable pageable);

    UserViewDto getById(long id);

    CustomerDto getCustomerById(Long id);

    EmployeeDto getEmployeeById(Long id);

    UserDto save(UserDto user);

    long login(CredentialsDto credentials);

    void updateCredentials(UpdateCredentialsDto dto);

    void replenishBalance(long customerId, BigDecimal amount);

    void createDefaultAdmin();

    void generateCustomers(int quantity, boolean active);

    void generateEmployees(int quantity, boolean active);
}
