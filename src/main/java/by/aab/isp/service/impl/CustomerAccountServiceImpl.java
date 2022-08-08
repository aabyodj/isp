package by.aab.isp.service.impl;

import by.aab.isp.dao.CustomerAccountDao;
import by.aab.isp.dao.DaoException;
import by.aab.isp.entity.CustomerAccount;
import by.aab.isp.entity.User;
import by.aab.isp.service.CustomerAccountService;
import by.aab.isp.service.ServiceException;

import java.util.Optional;

public class CustomerAccountServiceImpl implements CustomerAccountService {

    private final CustomerAccountDao accountDao;

    public CustomerAccountServiceImpl(CustomerAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public CustomerAccount getByUser(User user) {
        try {
            Optional<CustomerAccount> result = accountDao.findByUser(user);
            if (result.isPresent()) return result.get();
            if (user.getRole() != User.Role.CUSTOMER) {
                throw new ServiceException("Cannot create customer account for " + user.getRole());
            }
            return new CustomerAccount(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public CustomerAccount save(CustomerAccount account) {
        try {
            if (account.getId() == 0 || accountDao.countByUserId(account.getId()) < 1) {
                account = accountDao.save(account);
            } else {
                accountDao.update(account);
            }
            return account;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
