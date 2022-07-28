package by.aab.isp.dao;

import by.aab.isp.entity.Tariff;

public interface TariffDao extends CrudRepository<Tariff> {

    static TariffDao getInstance() {
        return DaoFactory.getInstance().getDao(TariffDao.class);
    }
}
