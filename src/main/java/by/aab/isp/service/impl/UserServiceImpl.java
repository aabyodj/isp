package by.aab.isp.service.impl;

import by.aab.isp.dao.UserDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.UserService;

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
}
