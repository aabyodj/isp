package by.aab.isp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import by.aab.isp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndActive(String email, boolean active);

}
