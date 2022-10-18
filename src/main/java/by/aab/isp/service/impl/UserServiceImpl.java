package by.aab.isp.service.impl;

import static by.aab.isp.Const.DEFAULT_ADMIN_EMAIL;
import static by.aab.isp.Const.DEFAULT_ADMIN_PASSWORD;
import static by.aab.isp.Const.LDT_FOR_AGES;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import by.aab.isp.aspect.AutoLogged;
import by.aab.isp.dto.user.CredentialsDto;
import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UpdateCredentialsDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Tariff;
import by.aab.isp.entity.User;
import by.aab.isp.repository.CustomerRepository;
import by.aab.isp.repository.EmployeeRepository;
import by.aab.isp.repository.OrderOffsetLimit;
import by.aab.isp.repository.TariffRepository;
import by.aab.isp.repository.UserRepository;
import by.aab.isp.service.Pagination;
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
    private static final List<OrderOffsetLimit.Order> ORDER_BY_EMAIL = List.of(
            new OrderOffsetLimit.Order("email", true)
    );

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;
    private final SubscriptionService subscriptionService;

    @AutoLogged
    @Override
    public List<CustomerDto> getAllCustomers(Pagination pagination) {
        long count = customerRepository.count();
        pagination.setTotalItemsCount(count);
        long offset = pagination.getOffset();
        if (offset >= count) {
            pagination.setPageNumber(pagination.getLastPageNumber());
        } else {
            pagination.setOffset(Long.max(0, offset));
        }
        if (count > 0) {
            OrderOffsetLimit orderOffsetLimit = new OrderOffsetLimit();
            orderOffsetLimit.setOrderList(ORDER_BY_EMAIL);
            orderOffsetLimit.setOffset(pagination.getOffset());
            orderOffsetLimit.setLimit(pagination.getPageSize());
            return customerRepository.findAll(orderOffsetLimit)
                    .stream()
                    .map(this::toCustomerDto)
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    @AutoLogged
    @Override
    public List<EmployeeDto> getAllEmployees(Pagination pagination) {
        long count = employeeRepository.count();
        pagination.setTotalItemsCount(count);
        long offset = pagination.getOffset();
        if (offset >= count) {
            pagination.setPageNumber(pagination.getLastPageNumber());
        } else {
            pagination.setOffset(Long.max(0, offset));
        }
        if (count > 0) {
            OrderOffsetLimit orderOffsetLimit = new OrderOffsetLimit();
            orderOffsetLimit.setOrderList(ORDER_BY_EMAIL);
            orderOffsetLimit.setOffset(pagination.getOffset());
            orderOffsetLimit.setLimit(pagination.getPageSize());
            return employeeRepository.findAll(orderOffsetLimit)
                    .stream()
                    .map(this::toEmployeeDto)
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    @AutoLogged
    @Override
    public UserDto getById(long id) {
        User user = userRepository.findById(id).orElseThrow();
        return toUserDto(user);
    }

    private UserDto toUserDto(User user) {
        UserDto dto;
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            CustomerDto customerDto = new CustomerDto();
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

    private CustomerDto toCustomerDto(Customer customer) {
        return (CustomerDto) toUserDto(customer);
    }

    private EmployeeDto toEmployeeDto(Employee employee) {
        return (EmployeeDto) toUserDto(employee);
    }

    @AutoLogged
    @Override
    public CustomerDto getCustomerById(Long id) {
        CustomerDto customer;
        if (null == id) {
            customer = new CustomerDto();
            customer.setBalance(BigDecimal.ZERO);
            customer.setPermittedOverdraft(BigDecimal.ZERO);
        } else {
            customer = (CustomerDto) toUserDto(customerRepository.findById(id).orElseThrow());
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
    public UserDto save(UserDto dto) {
        User user;
        if (dto.getId() != null) {
            user = userRepository.findById(dto.getId()).orElseThrow();
            setFields(dto, user);
            userRepository.update(user);
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

    private void setFields(UserDto dto, User user) {
        if (dto instanceof CustomerDto) {
            CustomerDto customerDto = (CustomerDto) dto;
            Customer customer = (Customer) user;
            customer.setBalance(customerDto.getBalance());
            customer.setPermittedOverdraft(customerDto.getPermittedOverdraft());
            LocalDateTime payoffDate = customerDto.getPayoffDate();
            customer.setPayoffDate(payoffDate != null ? payoffDate : LDT_FOR_AGES);
        } else if (dto instanceof EmployeeDto) {
            EmployeeDto employeeDto = (EmployeeDto) dto;
            Employee employee = (Employee) user;
            if (!isActiveAdmin(employeeDto) && noMoreAdmins(employeeDto)) {
                throw new ServiceException("Unable to delete last admin");
            }
            employee.setRole(employeeDto.getRole());
        }
        String email = dto.getEmail().strip();
        if (!email.equals(user.getEmail())) {
            validateEmailConstraints(email);
            user.setEmail(email);
        }
        String password = dto.getPassword();
        if (password != null) {
            validatePasswordConstraints(password);
            user.setPasswordHash(hashPassword(password));
        }
        user.setActive(dto.isActive());
    }

    private User toNewUser(UserDto dto) {
        User user;
        if (dto instanceof CustomerDto) {
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

    private void validateEmailConstraints(String email) {
        // TODO Auto-generated method stub
    }

    private void validatePasswordConstraints(String password) {
        if (!isStrongPassword(password)) {
            throw new ServiceException("Password is too weak");
        }
    }

    private boolean isStrongPassword(String password) { //TODO: implement password strength criteria
        return password.length() > 0;
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
    public UserDto login(CredentialsDto credentials) {
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
        return toUserDto(user);
    }

    @AutoLogged
    @Override
    @Transactional
    public void updateCredentials(UpdateCredentialsDto dto) {
        byte[] hash = hashWithDelay(dto.getCurrentPassword());
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        if (!Arrays.equals(user.getPasswordHash(), hash)) {
            throw new ServiceException("Wrong current password");   //TODO: maybe invalidate the user session?
        }
        String email = dto.getEmail().strip();
        if (!email.equals(user.getEmail())) {
            validateEmailConstraints(email);
            user.setEmail(email);
        }
        String password = dto.getNewPassword();
        if (password != null) {
            validatePasswordConstraints(password);
            user.setPasswordHash(hashPassword(password));
        }
        userRepository.update(user);
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
            customer.setPayoffDate(null);
        }
        userRepository.update(customer);
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
        Tariff[] tariffs = tariffRepository.findByActive(true)
                .stream()
                .toArray(Tariff[]::new);
        Random random = new Random();
        int i = 1;
        while (quantity > 0) {
            String emailName = GENERATED_CUSTOMER_EMAIL_NAME + i++;
            Customer customer = new Customer();
            customer.setEmail(emailName + GENERATED_EMAIL_DOMAIN);
            customer.setPasswordHash(hashPassword(emailName));
            customer.setBalance(BigDecimal.valueOf(
                    random.nextDouble() * (GENERATED_CUSTOMER_MAX_BALANCE - GENERATED_CUSTOMER_MIN_BALANCE)
                            + GENERATED_CUSTOMER_MIN_BALANCE));
            customer.setPermittedOverdraft(BigDecimal.ZERO);    //TODO: let managers set default permitted overdraft
            customer.setPayoffDate(LDT_FOR_AGES);
            customer.setActive(active);
            try {
                customer = (Customer) userRepository.save(customer);
                int tariffIndex = random.nextInt(tariffs.length + 1);
                if (tariffIndex < tariffs.length) {
                    subscriptionService.subscribe(customer.getId(), tariffs[tariffIndex].getId());
                }
                quantity--;
            } catch (Exception ignore) {
            }
        }
    }

    @AutoLogged
    @Override
    public void generateEmployees(int quantity, boolean active) {
        Employee.Role[] roles = Employee.Role.values();
        Random random = new Random();
        int i = 1;
        while (quantity > 0) {
            String emailName = GENERATED_EMPLOYEE_EMAIL_NAME + i++;
            Employee employee = new Employee();
            employee.setEmail(emailName + GENERATED_EMAIL_DOMAIN);
            employee.setPasswordHash(hashPassword(emailName));
            employee.setRole(roles[random.nextInt(roles.length)]);
            employee.setActive(active);
            try {
                userRepository.save(employee);
                quantity--;
            } catch (Exception ignore) {
            }
        }
    }
}
