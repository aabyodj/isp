package by.aab.isp.service.impl;

import by.aab.isp.dao.TariffDao;
import by.aab.isp.entity.Tariff;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.TariffService;

import java.math.BigDecimal;
import java.util.Random;

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

    private static final int MBIT_S = 1024;
    private static final Integer[] BANDWIDTH = {
            MBIT_S, 5 * MBIT_S, 10 * MBIT_S, 20 * MBIT_S, 50 * MBIT_S,
            100 * MBIT_S, 200 * MBIT_S, 500 * MBIT_S, 1024 * MBIT_S};
    private static final long MB = 1024 * 1024;
    private static final long TB = 1024 * MB;
    private static final Long[] TRAFFIC = {
            50 * MB, 100 * MB, 200 * MB, 500 * MB, TB, 2 * TB, 5 * TB, 10 * TB, 20 * TB, 30 * TB};
    private static final double MAX_PRICE = 49.95;

    @Override
    public void generateTariffs(int quantity, boolean active) {
        for (int i = 1; i <= quantity; i++) {
            Tariff tariff = new Tariff();
            tariff.setName("Generated " + i);
            tariff.setDescription("Automatically generated tariff #" + i);
            Random random = new Random();
            if (random.nextBoolean()) {
                int index = random.nextInt(BANDWIDTH.length);
                tariff.setBandwidth(BANDWIDTH[index]);
                tariff.setIncludedTraffic(null);
                tariff.setPrice(new BigDecimal(MAX_PRICE * (index + 1) / BANDWIDTH.length));
            } else {
                int index = random.nextInt(TRAFFIC.length);
                tariff.setIncludedTraffic(TRAFFIC[index]);
                tariff.setBandwidth(null);
                tariff.setPrice(new BigDecimal(MAX_PRICE * (index + 1) / TRAFFIC.length));
            }
            tariff.setActive(active);
            tariffDao.save(tariff);
        }
    }

}
