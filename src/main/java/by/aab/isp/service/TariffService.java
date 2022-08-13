package by.aab.isp.service;

import by.aab.isp.entity.Tariff;

public interface TariffService {
    
    Iterable<Tariff> getAll();
    
    Tariff getById(long id);

    Tariff save(Tariff tariff);
}
