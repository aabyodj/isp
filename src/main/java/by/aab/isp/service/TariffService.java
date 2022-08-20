package by.aab.isp.service;

import by.aab.isp.entity.Tariff;

public interface TariffService {
    
    Iterable<Tariff> getAll();

    Iterable<Tariff> getForHomepage();
    
    Tariff getById(Long id);

    Tariff save(Tariff tariff);
}
