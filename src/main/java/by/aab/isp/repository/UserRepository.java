package by.aab.isp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.aab.isp.repository.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndActive(String email, boolean active);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Optional<Long> findIdByEmail(@Param("email") String email);

    @Query("UPDATE User u SET u.active = :active WHERE u.id = :id")
    long setActiveById(@Param("id") long id, @Param("active") boolean active);

    @Query("SELECT count(*) FROM User u WHERE u.email = :email")
    long countByEmail(@Param("email") String email);

}
