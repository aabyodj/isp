package by.aab.isp.service;

import by.aab.isp.entity.CustomerAccount;
import by.aab.isp.entity.User;

public interface CustomerAccountService {

    CustomerAccount getByUser(User user);

    CustomerAccount save(CustomerAccount account);

}
