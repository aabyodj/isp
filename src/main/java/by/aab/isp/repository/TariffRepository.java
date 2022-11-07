package by.aab.isp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.aab.isp.entity.Tariff;

public interface TariffRepository extends JpaRepository<Tariff, Long> {

    Page<Tariff> findByActive(boolean active, Pageable pageable);

    List<Tariff> findByActive(boolean active);

    @Query("FROM Tariff t WHERE t.active = true AND NOT t.id IN "
            + "(SELECT DISTINCT s.tariff.id FROM Subscription s WHERE s.customer.id = :customerId AND s.activeSince <= :instant AND s.activeUntil >= :instant)")
    List<Tariff> findInactiveForCustomer(@Param("customerId") long customerId, @Param("instant") LocalDateTime instant);

    @Query("UPDATE Tariff t SET t.active = :active WHERE t.id = :id")
    long setActiveById(@Param("id") long id, @Param("active") boolean active);

    @Query("SELECT count(*) FROM Tariff t WHERE t.name = :name")
    long countByName(@Param("name") String name);

}
