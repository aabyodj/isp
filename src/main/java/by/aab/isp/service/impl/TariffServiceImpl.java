package by.aab.isp.service.impl;

import java.util.Collection;

import by.aab.isp.dao.TariffDao;
import by.aab.isp.entity.Tariff;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.TariffService;

public class TariffServiceImpl implements TariffService {
    
    private final TariffDao tariffDao;
    
    public TariffServiceImpl(TariffDao tariffDao) {
        this.tariffDao = tariffDao;
    }

    @Override
    public Collection<Tariff> getAll() {
        try {
            return tariffDao.findAll();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Tariff getById(long id) {
        try {
        return tariffDao.findById(id).orElseThrow();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}
