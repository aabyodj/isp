package by.aab.isp.dao;

import by.aab.isp.entity.Promotion;

import java.time.Instant;

public interface PromotionDao extends CrudRepository<Promotion> {
    Iterable<Promotion> findByActivePeriodContains(Instant instant);
}
