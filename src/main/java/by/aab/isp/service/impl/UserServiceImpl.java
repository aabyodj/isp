package by.aab.isp.service.impl;

import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.UnauthorizedException;
import by.aab.isp.service.UserService;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ADMIN_EMAIL = "admin@example.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final long LOGIN_TIMEOUT = 2000;
    private static final double TIMEOUT_SHIFT_FACTOR = .5;

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Iterable<Customer> getAllCustomers() {
        return userDao.findAllCustomers();
    }

    @Override
    public Iterable<Employee> getAllEmployees() {
        return userDao.findAllEmployees();
    }

    @Override
    public User getById(long id) {
        User user = userDao.findById(id).orElseThrow();
        user.setPasswordHash(null);
        return user;
    }

    @Override
    public Customer getCustomerById(long id) {
        Customer customer;
        if (0 == id) {
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
    public Employee getEmployeeById(long id) {
        Employee employee;
        if (0 == id) {
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
        if (user.getId() == 0) {
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
            if (!Arrays.equals(hash, savedHash)) user = null;
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
            Employee admin = getEmployeeById(0);
            admin.setEmail(DEFAULT_ADMIN_EMAIL);
            admin.setPasswordHash(hashPassword(DEFAULT_ADMIN_PASSWORD));
            admin.setRole(Employee.Role.ADMIN);
            admin.setActive(true);
            userDao.save(admin);
        }
    }
}
