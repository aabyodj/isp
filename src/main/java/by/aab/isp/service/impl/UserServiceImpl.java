package by.aab.isp.service.impl;

import static by.aab.isp.Const.DEFAULT_ADMIN_EMAIL;
import static by.aab.isp.Const.DEFAULT_ADMIN_PASSWORD;
import static by.aab.isp.Const.LDT_FOR_AGES;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import by.aab.isp.aspect.AutoLogged;
import by.aab.isp.converter.user.CustomerToCustomerViewDtoConverter;
import by.aab.isp.converter.user.EmployeeToEmployeeViewDtoConverter;
import by.aab.isp.converter.user.UserToUserViewDtoConverter;
import by.aab.isp.dto.user.LoginCredentialsDto;
import by.aab.isp.dto.user.CustomerEditDto;
import by.aab.isp.dto.user.CustomerViewDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.EmployeeViewDto;
import by.aab.isp.dto.user.UpdateCredentialsDto;
import by.aab.isp.dto.user.UserEditDto;
import by.aab.isp.dto.user.UserViewDto;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Tariff;
import by.aab.isp.entity.User;
import by.aab.isp.repository.CustomerRepository;
import by.aab.isp.repository.EmployeeRepository;
import by.aab.isp.repository.TariffRepository;
import by.aab.isp.repository.UserRepository;
import by.aab.isp.service.NotFoundException;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.UnauthorizedException;
import by.aab.isp.service.UserService;
import lombok.RequiredArgsConstructor;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final long LOGIN_TIMEOUT = 2000;
    private static final double TIMEOUT_SHIFT_FACTOR = .5;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;
    private final SubscriptionService subscriptionService;
    private final CustomerToCustomerViewDtoConverter customerViewConverter;
    private final EmployeeToEmployeeViewDtoConverter employeeViewConverter;
    private final UserToUserViewDtoConverter userViewConverter;

    @AutoLogged
    @Override
    public Page<CustomerViewDto> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerViewConverter::convert);
    }

    @AutoLogged
    @Override
    public Page<EmployeeViewDto> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(employeeViewConverter::convert);
    }

    @AutoLogged
    @Override
    public UserViewDto getById(long id) {
        return userRepository.findById(id)
                .map(userViewConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    private UserEditDto toUserDto(User user) {
        UserEditDto dto;
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            CustomerEditDto customerDto = new CustomerEditDto();
            customerDto.setBalance(customer.getBalance());
            customerDto.setPermittedOverdraft(customer.getPermittedOverdraft());
            LocalDateTime payoffDate = customer.getPayoffDate();
            customerDto.setPayoffDate(payoffDate.isBefore(LDT_FOR_AGES) ? payoffDate : null);
            dto = customerDto;
        } else if (user instanceof Employee) {
            Employee employee = (Employee) user;
            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setRole(employee.getRole());
            dto = employeeDto;
        } else {
            throw new ServiceException("Converter is undefined for entity " + user.getClass());
        }
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setActive(user.isActive());
        return dto;
    }

    @AutoLogged
    @Override
    public CustomerEditDto getCustomerById(Long id) {
        CustomerEditDto customer;
        if (null == id) {
            customer = new CustomerEditDto();
            customer.setBalance(BigDecimal.ZERO);
            customer.setPermittedOverdraft(BigDecimal.ZERO);
        } else {
            customer = (CustomerEditDto) toUserDto(customerRepository.findById(id).orElseThrow());
        }
        return customer;
    }

    @AutoLogged
    @Override
    public EmployeeDto getEmployeeById(Long id) {
        EmployeeDto employee;
        if (null == id) {
            employee = new EmployeeDto();
            employee.setRole(Employee.Role.MANAGER);
        } else {
            employee = (EmployeeDto) toUserDto(employeeRepository.findById(id).orElseThrow());
        }
        return employee;
    }

    @AutoLogged
    @Override
    @Transactional
    public UserEditDto save(UserEditDto dto) {
        User user;
        if (dto.getId() != null) {
            user = userRepository.findById(dto.getId()).orElseThrow();
            setFields(dto, user);
            userRepository.save(user);
        } else {
            if (null == dto.getPassword()) {
                throw new ServiceException("Password required");
            }
            user = toNewUser(dto);
            user = userRepository.save(user);
            dto.setId(user.getId());
        }
        return dto;
    }

    private void setFields(UserEditDto dto, User user) {
        if (dto instanceof CustomerEditDto) {
            CustomerEditDto customerDto = (CustomerEditDto) dto;
            Customer customer = (Customer) user;
            customer.setBalance(customerDto.getBalance());
            customer.setPermittedOverdraft(customerDto.getPermittedOverdraft());
            LocalDateTime payoffDate = customerDto.getPayoffDate();
            customer.setPayoffDate(payoffDate != null ? payoffDate : LDT_FOR_AGES);
        } else if (dto instanceof EmployeeDto) {
            EmployeeDto employeeDto = (EmployeeDto) dto;
            Employee employee = (Employee) user;
            if (employeeDto.getId() != null && !isActiveAdmin(employeeDto) && noMoreAdmins(employeeDto)) {
                throw new ServiceException("Unable to delete last admin");
            }
            employee.setRole(employeeDto.getRole());
        }
        String email = dto.getEmail().strip();
        if (!email.equals(user.getEmail())) {
            user.setEmail(email);
        }
        String password = dto.getPassword();
        if (password != null && !password.isBlank()) {
            user.setPasswordHash(hashPassword(password));
        }
        user.setActive(dto.isActive());
    }

    private User toNewUser(UserEditDto dto) {
        User user;
        if (dto instanceof CustomerEditDto) {
            user = new Customer();
        } else if (dto instanceof EmployeeDto) {
            user = new Employee();
        } else {
            throw new ServiceException("Converter is undefined for DTO " + dto.getClass());
        }
        setFields(dto, user);
        return user;
    }

    private static boolean isActiveAdmin(EmployeeDto employee) {
        return employee.getRole() == Employee.Role.ADMIN && employee.isActive();
    }

    private boolean noMoreAdmins(EmployeeDto employee) {
        return employeeRepository.countByNotIdAndRoleAndActive(employee.getId(), Employee.Role.ADMIN, true) < 1;
    }

    private byte[] hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(password.getBytes());
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }
    }

    private byte[] hashWithDelay(String password) {
        long timeout = (long) (LOGIN_TIMEOUT * (Math.random() + TIMEOUT_SHIFT_FACTOR));
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException ignore) {
        }
        return hashPassword(password);
    }

    @AutoLogged
    @Override
    public long login(LoginCredentialsDto credentials) {
        byte[] hash = hashWithDelay(credentials.getPassword());
        User user = userRepository.findByEmailAndActive(credentials.getEmail(), true).orElse(null);
        if (user != null) {
            byte[] savedHash = user.getPasswordHash();
            if (!Arrays.equals(hash, savedHash)) {
                user = null;
            }
        }
        if (null == user) {
            throw new UnauthorizedException(credentials.getEmail());
        }
        return user.getId();
    }

    @AutoLogged
    @Override
    @Transactional
    public void updateCredentials(UpdateCredentialsDto dto) {
        byte[] hash = hashWithDelay(dto.getCurrentPassword());
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        if (!Arrays.equals(user.getPasswordHash(), hash)) {
            throw new UnauthorizedException("Wrong current password");
        }
        String email = dto.getEmail().strip();
        if (!email.equals(user.getEmail())) {
            user.setEmail(email);
        }
        String password = dto.getNewPassword();
        if (password != null && !password.isBlank()) {
            user.setPasswordHash(hashPassword(password));
        }
        userRepository.save(user);
    }

    @AutoLogged
    @Override
    public void replenishBalance(long customerId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("Cannot replenish balance by nonpositive amount");
        }
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        BigDecimal balance = customer.getBalance();
        balance = balance.add(amount);
        customer.setBalance(balance);
        if (balance.compareTo(BigDecimal.ZERO) >= 0) {
            customer.setPayoffDate(LDT_FOR_AGES);
        }
        userRepository.save(customer);
    }

    @AutoLogged
    @PostConstruct
    @Override
    public void createDefaultAdmin() {
        if (employeeRepository.countByRoleAndActive(Employee.Role.ADMIN, true) < 1) {
            Employee admin = new Employee();
            admin.setEmail(DEFAULT_ADMIN_EMAIL);
            admin.setPasswordHash(hashPassword(DEFAULT_ADMIN_PASSWORD));
            admin.setRole(Employee.Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
        }
    }

    private static final String GENERATED_EMAIL_DOMAIN = "@generated.example.com";
    private static final String GENERATED_CUSTOMER_EMAIL_NAME = "customer";
    private static final double GENERATED_CUSTOMER_MIN_BALANCE = -10.0;
    private static final double GENERATED_CUSTOMER_MAX_BALANCE = 100;
    private static final String GENERATED_EMPLOYEE_EMAIL_NAME = "employee";

    @Transactional
    @AutoLogged
    @Override
    public void generateCustomers(int quantity, boolean active) {
        if (quantity < 1) {
            return;
        }
        Tariff[] tariffs = tariffRepository.findByActive(true)
                .stream()
                .toArray(Tariff[]::new);
        Random random = new Random();
        int i = 0;
        while (quantity > 0) {
            i++;
            String emailName = GENERATED_CUSTOMER_EMAIL_NAME + i;
            String generatedEmail = emailName + GENERATED_EMAIL_DOMAIN;
            if (userRepository.countByEmail(generatedEmail) > 0) {
                continue;
            }
            Customer customer = new Customer();
            customer.setEmail(generatedEmail);
            customer.setPasswordHash(hashPassword(emailName));
            customer.setBalance(BigDecimal.valueOf(
                    random.nextDouble() * (GENERATED_CUSTOMER_MAX_BALANCE - GENERATED_CUSTOMER_MIN_BALANCE)
                            + GENERATED_CUSTOMER_MIN_BALANCE));
            customer.setPermittedOverdraft(BigDecimal.ZERO);    //TODO: let managers set default permitted overdraft
            customer.setPayoffDate(LDT_FOR_AGES);
            customer.setActive(active);
            customer = (Customer) userRepository.save(customer);
            int tariffIndex = random.nextInt(tariffs.length + 1);
            if (tariffIndex < tariffs.length) {
                subscriptionService.subscribe(customer.getId(), tariffs[tariffIndex].getId());
            }
            quantity--;
        }
    }

    @AutoLogged
    @Override
    public void generateEmployees(int quantity, boolean active) {
        if (quantity < 1) {
            return;
        }
        Employee.Role[] roles = Employee.Role.values();
        Random random = new Random();
        int i = 0;
        while (quantity > 0) {
            i++;
            String emailName = GENERATED_EMPLOYEE_EMAIL_NAME + i;
            String generatedEmail = emailName + GENERATED_EMAIL_DOMAIN;
            if (userRepository.countByEmail(generatedEmail) > 0) {
                continue;
            }
            Employee employee = new Employee();
            employee.setEmail(generatedEmail);
            employee.setPasswordHash(hashPassword(emailName));
            employee.setRole(roles[random.nextInt(roles.length)]);
            employee.setActive(active);
            userRepository.save(employee);
            quantity--;
        }
    }
}
