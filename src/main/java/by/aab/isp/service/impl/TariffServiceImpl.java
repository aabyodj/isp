package by.aab.isp.service.impl;

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
    public Iterable<Tariff> getAll() {
        try {
            return tariffDao.findAll();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Iterable<Tariff> getForHomepage() {
        return tariffDao.findByActive(true);
    }

    @Override
    public Tariff getById(Long id) {
        return id != null ? tariffDao.findById(id).orElseThrow()
                          : new Tariff();
    }

    @Override
    public Tariff save(Tariff tariff) {
        tariff.setName(tariff.getName().strip());
        tariff.setDescription(tariff.getDescription().strip());
        try {
            if (tariff.getId() == null) {
                return tariffDao.save(tariff);
            } else {
               tariffDao.update(tariff);
                return tariff;
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}
