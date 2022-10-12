package by.aab.isp.repository;

import java.util.List;

import by.aab.isp.entity.Tariff;

public interface TariffRepository extends CrudRepository<Tariff> {

    List<Tariff> findByActive(boolean active);

}
