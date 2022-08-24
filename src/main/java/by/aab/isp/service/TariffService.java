package by.aab.isp.service;

import by.aab.isp.entity.Tariff;

public interface TariffService {
    
    Iterable<Tariff> getAll();

    Iterable<Tariff> getAll(Pagination pagination);

    Iterable<Tariff> getActive();

    Iterable<Tariff> getForHomepage();
    
    Tariff getById(Long id);

    Tariff save(Tariff tariff);

    void generateTariffs(int quantity, boolean active);
}
