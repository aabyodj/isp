package by.aab.isp.service.impl;

import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.UserService;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService {

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
    public User save(User user) {
        //TODO: validate email
        user.setEmail(user.getEmail().strip());
        try {
            if (user.getId() == 0) {
                return userDao.save(user);
            } else {
                userDao.update(user);
                return user;
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User login(String email, String password) {
        try {
            //TODO: validate password
            return userDao.findByEmail(email).orElseThrow();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
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
}
