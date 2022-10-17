package by.aab.isp.repository;

import java.time.LocalDateTime;
import java.util.List;

import by.aab.isp.entity.Tariff;

public interface TariffRepository extends CrudRepository<Tariff> {

    List<Tariff> findByActive(boolean active);

    List<Tariff> findInactiveForCustomer(long customerId, LocalDateTime moment);

}
