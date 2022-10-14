package by.aab.isp.repository;

import java.time.LocalDateTime;
import java.util.List;

import by.aab.isp.entity.Promotion;

public interface PromotionRepository extends CrudRepository<Promotion> {

    List<Promotion> findByActivePeriodContains(
            LocalDateTime instant, OrderOffsetLimit orderOffsetLimit);

}
