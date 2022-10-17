package by.aab.isp.service.impl;

import static by.aab.isp.Const.BANDWIDTH_UNLIMITED;
import static by.aab.isp.Const.TRAFFIC_UNLIMITED;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import by.aab.isp.aspect.AutoLogged;
import by.aab.isp.dto.converter.TariffConverter;
import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.entity.Tariff;
import by.aab.isp.repository.OrderOffsetLimit;
import by.aab.isp.repository.TariffRepository;
import by.aab.isp.service.Pagination;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.TariffService;
import lombok.RequiredArgsConstructor;

@Service("tariffService")
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {
    
    private final TariffRepository tariffRepository;
    private final TariffConverter tariffConverter;

    @AutoLogged
    @Override
    public List<ShowTariffDto> getAll() {
        try {
            return tariffRepository.findAll()
                    .stream()
                    .map(tariffConverter::toShowTariffDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @AutoLogged
    @Override
    public List<ShowTariffDto> getAll(Pagination pagination) {
        long count = tariffRepository.count();
        pagination.setTotalItemsCount(count);
        long offset = pagination.getOffset();
        if (offset >= count) {
            pagination.setPageNumber(pagination.getLastPageNumber());
        } else {
            pagination.setOffset(Long.max(0, offset));
        }
        if (count > 0) {
            OrderOffsetLimit orderOffsetLimit = new OrderOffsetLimit();
            orderOffsetLimit.setOffset(pagination.getOffset());
            orderOffsetLimit.setLimit(pagination.getPageSize());
            return tariffRepository.findAll(orderOffsetLimit) //TODO: add "order" field to Tariff
                    .stream()
                    .map(tariffConverter::toShowTariffDto)
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    @AutoLogged
    @Override
    public List<ShowTariffDto> getForHomepage() {
        return tariffRepository.findByActive(true)
                .stream()
                .map(tariffConverter::toShowTariffDto)
                .collect(Collectors.toList());
    }

    @AutoLogged
    @Override
    public Tariff getById(Long id) {
        return id != null ? tariffRepository.findById(id).orElseThrow()
                          : new Tariff();
    }

    @AutoLogged
    @Override
    public Tariff save(Tariff tariff) {
        tariff.setName(tariff.getName().strip());
        tariff.setDescription(tariff.getDescription().strip());
        try {
            if (tariff.getId() == null) {
                return tariffRepository.save(tariff);
            } else {
               tariffRepository.update(tariff);
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

    @AutoLogged
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
                tariff.setIncludedTraffic(TRAFFIC_UNLIMITED);
                tariff.setPrice(new BigDecimal(MAX_PRICE * (index + 1) / BANDWIDTH.length));
            } else {
                int index = random.nextInt(TRAFFIC.length);
                tariff.setIncludedTraffic(TRAFFIC[index]);
                tariff.setBandwidth(BANDWIDTH_UNLIMITED);
                tariff.setPrice(new BigDecimal(MAX_PRICE * (index + 1) / TRAFFIC.length));
            }
            tariff.setActive(active);
            tariffRepository.save(tariff);
        }
    }

    @Override
    public List<ShowTariffDto> getInactiveForCustomer(long customerId) {
        LocalDateTime now = LocalDateTime.now();
        return tariffRepository.findInactiveForCustomer(customerId, now)
                .stream()
                .map(tariffConverter::toShowTariffDto)
                .collect(Collectors.toList());
    }

}
