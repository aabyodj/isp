package by.aab.isp.repository;

import java.util.Optional;

import by.aab.isp.entity.User;

public interface UserRepository extends CrudRepository<User> {

    Optional<User> findByEmailAndActive(String email, boolean active);

}
