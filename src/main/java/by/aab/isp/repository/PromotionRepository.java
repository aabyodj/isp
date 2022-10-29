package by.aab.isp.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.aab.isp.entity.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("FROM Promotion p WHERE p.activeSince <= :instant AND p.activeUntil >= :instant")
    Page<Promotion> findByActivePeriodContains(
            @Param("instant") LocalDateTime instant, Pageable pageable);

    @Query("SELECT count(*) FROM Promotion p WHERE p.name = :name")
    long countByName(@Param("name") String name);

}
