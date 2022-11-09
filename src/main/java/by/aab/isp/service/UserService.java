package by.aab.isp.service;

import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import by.aab.isp.service.dto.user.CustomerEditDto;
import by.aab.isp.service.dto.user.CustomerViewDto;
import by.aab.isp.service.dto.user.EmployeeEditDto;
import by.aab.isp.service.dto.user.EmployeeViewDto;
import by.aab.isp.service.dto.user.LoginCredentialsDto;
import by.aab.isp.service.dto.user.UpdateCredentialsDto;
import by.aab.isp.service.dto.user.UserEditDto;
import by.aab.isp.service.dto.user.UserViewDto;

public interface UserService {

    Page<CustomerViewDto> getAllCustomers(Pageable pageable);

    Page<EmployeeViewDto> getAllEmployees(Pageable pageable);

    UserViewDto getById(long id);

    CustomerEditDto getCustomerById(Long id);

    EmployeeEditDto getEmployeeById(long id);

    UserEditDto save(UserEditDto user);

    void deactivate(long id);

    long login(LoginCredentialsDto credentials);

    void updateCredentials(UpdateCredentialsDto dto);

    void replenishBalance(long customerId, BigDecimal amount);

    void createDefaultAdmin();

    void generateCustomers(int quantity, boolean active);

    void generateEmployees(int quantity, boolean active);
}
