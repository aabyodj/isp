package by.aab.isp.service.impl;

import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.UnauthorizedException;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.UserService;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ADMIN_EMAIL = "admin@example.com";

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
        try {
            return userDao.findById(id).orElseThrow();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
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
        }
        return employee;
    }

    @Override
    public User save(User user) {
        //TODO: validate email
        user.setEmail(user.getEmail().strip());
        if (user.getId() == 0) {
            return userDao.save(user);
        } else {
            if (user instanceof Employee) {
                Employee employee = (Employee) user;
                if (
                        (employee.getRole() != Employee.Role.ADMIN || !employee.isActive())
                        && userDao.countByNotIdAndRoleAndActive(user.getId(), Employee.Role.ADMIN, true) < 1) {
                    throw new ServiceException("Unable to delete last admin");
                }
            }
            userDao.update(user);
            return user;
        }
    }

    @Override
    public User login(String email, String password) {
        //TODO: validate password
        return userDao.findByEmailAndActive(email, true)
                .orElseThrow(() -> new UnauthorizedException(email));
    }

    @Override
    public void updateCredentials(User user, String newEmail, String newPassword, String currentPassword) {
        //TODO: validate email and password
        user.setEmail(newEmail);
        userDao.update(user);
    }

    @Override
    public void replenishBalance(Customer customer, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("Cannot replenish balance by nonpositive amount");
        }
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
            admin.setRole(Employee.Role.ADMIN);
            admin.setActive(true);
            userDao.save(admin);
        }
    }
}
