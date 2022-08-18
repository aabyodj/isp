package by.aab.isp.dao;

import by.aab.isp.entity.Tariff;

public interface TariffDao extends CrudRepository<Tariff> {
    Iterable<Tariff> findByActive(boolean active);
}
