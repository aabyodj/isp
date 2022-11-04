package by.aab.isp.service;

import by.aab.isp.dto.user.LoginCredentialsDto;
import by.aab.isp.dto.user.CustomerEditDto;
import by.aab.isp.dto.user.CustomerViewDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.EmployeeViewDto;
import by.aab.isp.dto.user.UpdateCredentialsDto;
import by.aab.isp.dto.user.UserEditDto;
import by.aab.isp.dto.user.UserViewDto;

import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<CustomerViewDto> getAllCustomers(Pageable pageable);

    Page<EmployeeViewDto> getAllEmployees(Pageable pageable);

    UserViewDto getById(long id);

    CustomerEditDto getCustomerById(Long id);

    EmployeeDto getEmployeeById(Long id);

    UserEditDto save(UserEditDto user);

    long login(LoginCredentialsDto credentials);

    void updateCredentials(UpdateCredentialsDto dto);

    void replenishBalance(long customerId, BigDecimal amount);

    void createDefaultAdmin();

    void generateCustomers(int quantity, boolean active);

    void generateEmployees(int quantity, boolean active);
}
