package by.aab.isp.dao;

import by.aab.isp.entity.Tariff;

public interface TariffDao extends CrudRepository<Tariff> {

    Iterable<Tariff> findAll(long skip, int limit); //TODO: move this to CrudRepository

    Iterable<Tariff> findByActive(boolean active);
}
