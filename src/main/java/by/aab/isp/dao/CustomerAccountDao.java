package by.aab.isp.dao;

import by.aab.isp.entity.CustomerAccount;
import by.aab.isp.entity.User;

import java.util.Optional;

public interface CustomerAccountDao extends CrudRepository<CustomerAccount> {

    long countByUserId(long userId);

    Optional<CustomerAccount> findByUser(User user);

}
