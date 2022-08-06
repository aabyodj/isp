package by.aab.isp.dao;

import by.aab.isp.entity.User;

import java.util.Optional;

public interface UserDao extends CrudRepository<User> {

    Optional<User> findByEmail(String email);

    long countByRoleId(long roleId);

}
