package by.aab.isp.service;

import java.util.Collection;

import by.aab.isp.entity.Tariff;

public interface TariffService {
    
    Collection<Tariff> getAll();
    
    Tariff getById(long id);
    
    static TariffService getInstance() {
        return ServiceFactory.getInstance().getService(TariffService.class);
    }
    
}
