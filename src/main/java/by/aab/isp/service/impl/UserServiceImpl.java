package by.aab.isp.service.impl;

import by.aab.isp.dao.OrderOffsetLimit;
import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Tariff;
import by.aab.isp.entity.User;
import by.aab.isp.service.*;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.StreamSupport;

public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ADMIN_EMAIL = "admin@example.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final long LOGIN_TIMEOUT = 2000;
    private static final double TIMEOUT_SHIFT_FACTOR = .5;
    private static final List<OrderOffsetLimit.Order> ORDER_BY_EMAIL = List.of(
            new OrderOffsetLimit.Order("email", true)
    );

    private final UserDao userDao;
    private final TariffService tariffService;
    private final SubscriptionService subscriptionService;

    public UserServiceImpl(UserDao userDao, TariffService tariffService, SubscriptionService subscriptionService) {
        this.userDao = userDao;
        this.tariffService = tariffService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public Iterable<Customer> getAllCustomers(Pagination pagination) {
        long count = userDao.countCustomers();
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
            return userDao.findAllCustomers(orderOffsetLimit);
        } else {
            return List.of();
        }
    }

    @Override
    public Iterable<Employee> getAllEmployees(Pagination pagination) {
        long count = userDao.countEmployees();
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
            return userDao.findAllEmployees(orderOffsetLimit);
        } else {
            return List.of();
        }
    }

    @Override
    public User getById(long id) {
        User user = userDao.findById(id).orElseThrow();
        user.setPasswordHash(null);
        return user;
    }

    @Override
    public Customer getCustomerById(Long id) {
        Customer customer;
        if (null == id) {
            customer = new Customer();
            customer.setBalance(BigDecimal.ZERO);
            customer.setPermittedOverdraft(BigDecimal.ZERO);
        } else {
            customer = userDao.findCustomerById(id).orElseThrow();
            customer.setPasswordHash(null);
        }
        return customer;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Employee employee;
        if (null == id) {
            employee = new Employee();
            employee.setRole(Employee.Role.MANAGER);
        } else {
            employee = userDao.findEmployeeById(id).orElseThrow();
            employee.setPasswordHash(null);
        }
        return employee;
    }

    @Override
    public User save(User user, String password) {
        //TODO: validate email
        user.setEmail(user.getEmail().strip());
        if (null != password) {
            if (!isStrongPassword(password)) {
                throw new ServiceException("Password is too weak");
            }
            user.setPasswordHash(hashPassword(password));
        }
        if (user.getId() == null) {
            if (null == password) {
                throw new ServiceException("Password required");
            }
            user = userDao.save(user);
        } else {
            if (user instanceof Employee) {
                Employee employee = (Employee) user;
                if (!isActiveAdmin(employee) && noMoreAdmins(employee)) {
                    throw new ServiceException("Unable to delete last admin");
                }
            }
            userDao.update(user);
        }
        user.setPasswordHash(null);
        return user;
    }

    private static boolean isActiveAdmin(Employee employee) {
        return employee.getRole() == Employee.Role.ADMIN && employee.isActive();
    }

    private boolean noMoreAdmins(Employee employee) {
        return userDao.countByNotIdAndRoleAndActive(employee.getId(), Employee.Role.ADMIN, true) < 1;
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

    @Override
    public User login(String email, String password) {
        byte[] hash = hashWithDelay(password);
        User user = userDao.findByEmailAndActive(email, true).orElse(null);
        if (user != null) {
            byte[] savedHash = user.getPasswordHash();
            if (!Arrays.equals(hash, savedHash)) {
                user = null;
            }
        }
        if (null == user) {
            throw new UnauthorizedException(email);
        }
        user.setPasswordHash(null);
        return user;
    }

    @Override
    public void updateCredentials(User user, String newEmail, String newPassword, String currentPassword) {
        byte[] hash = hashWithDelay(currentPassword);
        user = userDao.findById(user.getId()).orElseThrow();
        if (!Arrays.equals(user.getPasswordHash(), hash)) {
            throw new ServiceException("Wrong current password");   //TODO: maybe invalidate the user session?
        }
        user.setEmail(newEmail);
        save(user, newPassword);
    }

    @Override
    public void replenishBalance(Customer customer, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("Cannot replenish balance by nonpositive amount");
        }
        customer = userDao.findCustomerById(customer.getId()).orElseThrow();
        BigDecimal balance = customer.getBalance();
        balance = balance.add(amount);
        customer.setBalance(balance);
        if (balance.compareTo(BigDecimal.ZERO) >= 0) {
            customer.setPayoffDate(null);
        }
        userDao.update(customer);
    }

    @Override
    public void createDefaultAdmin() {
        if (userDao.countByRoleAndActive(Employee.Role.ADMIN, true) < 1) {
            Employee admin = getEmployeeById(null);
            admin.setEmail(DEFAULT_ADMIN_EMAIL);
            admin.setPasswordHash(hashPassword(DEFAULT_ADMIN_PASSWORD));
            admin.setRole(Employee.Role.ADMIN);
            admin.setActive(true);
            userDao.save(admin);
        }
    }

    private static final String GENERATED_EMAIL_DOMAIN = "@generated.example.com";
    private static final String GENERATED_CUSTOMER_EMAIL_NAME = "customer";
    private static final double GENERATED_CUSTOMER_MIN_BALANCE = -10.0;
    private static final double GENERATED_CUSTOMER_MAX_BALANCE = 100;
    private static final String GENERATED_EMPLOYEE_EMAIL_NAME = "employee";

    @Override
    public void generateCustomers(int quantity, boolean active) {
        Tariff[] tariffs = StreamSupport
                .stream(tariffService.getActive().spliterator(), true)
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
            customer.setActive(active);
            try {
                customer = (Customer) userDao.save(customer);
                int tariffIndex = random.nextInt(tariffs.length + 1);
                if (tariffIndex < tariffs.length) {
                    subscriptionService.subscribe(customer, tariffs[tariffIndex].getId());
                }
                quantity--;
            } catch (Exception ignore) {
            }
        }
    }

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
                userDao.save(employee);
                quantity--;
            } catch (Exception ignore) {
            }
        }
    }
}
