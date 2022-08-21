package by.aab.isp.dao;

import by.aab.isp.entity.Promotion;

import java.time.LocalDateTime;

public interface PromotionDao extends CrudRepository<Promotion> {
    Iterable<Promotion> findByActivePeriodContainsOrderBySinceReversedThenByUntil(
            LocalDateTime instant, long skip, int limit);
}
