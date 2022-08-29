package by.aab.isp.dao;

import by.aab.isp.entity.Promotion;

import java.time.LocalDateTime;

public interface PromotionDao extends CrudRepository<Promotion> {

    Iterable<Promotion> findByActivePeriodContains(
            LocalDateTime instant, OrderOffsetLimit orderOffsetLimit);
}
