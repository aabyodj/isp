package by.aab.isp.service.impl;

import static by.aab.isp.Const.BANDWIDTH_UNLIMITED;
import static by.aab.isp.Const.TRAFFIC_UNLIMITED;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import by.aab.isp.aspect.AutoLogged;
import by.aab.isp.dto.tariff.TariffEditDto;
import by.aab.isp.dto.tariff.TariffViewDto;
import by.aab.isp.entity.Tariff;
import by.aab.isp.repository.TariffRepository;
import by.aab.isp.service.NotFoundException;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.TariffService;
import lombok.RequiredArgsConstructor;

@Service("tariffService")
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    private final ConversionService conversionService;

    @AutoLogged
    @Override
    public List<TariffViewDto> getAll() {
        try {
            return tariffRepository.findAll()
                    .stream()
                    .map(tariff -> conversionService.convert(tariff, TariffViewDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @AutoLogged
    @Override
    public Page<TariffViewDto> getAll(Pageable pageable) {
        return tariffRepository.findAll(pageable)
                .map(tariff -> conversionService.convert(tariff, TariffViewDto.class));
    }

    @AutoLogged
    @Override
    public Page<TariffViewDto> getActive(Pageable pageable) {
        return tariffRepository.findByActive(true, pageable)
                .map(tariff -> conversionService.convert(tariff, TariffViewDto.class));
    }

    @AutoLogged
    @Override
    public List<TariffViewDto> getActive() {
        return tariffRepository.findByActive(true)
                .stream()
                .map(tariff -> conversionService.convert(tariff, TariffViewDto.class))
                .collect(Collectors.toList());
    }

    @AutoLogged
    @Override
    public TariffEditDto getById(long id) {
        return tariffRepository.findById(id)
                .map(tariff -> conversionService.convert(tariff, TariffEditDto.class))
                .orElseThrow(NotFoundException::new);
    }

    @AutoLogged
    @Override
    public TariffEditDto save(TariffEditDto dto) {
        dto.setName(dto.getName().strip());
        dto.setDescription(dto.getDescription().strip());
        Tariff tariff = conversionService.convert(dto, Tariff.class);
        tariffRepository.save(tariff);
        dto.setId(tariff.getId());
        return dto;
    }

    @AutoLogged
    @Override
    public void deactivate(long tariffId) {
        if (tariffRepository.setActiveById(tariffId, false) == 0) {
            throw new NotFoundException();
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
        int i = 0;
        while (quantity > 0) {
            i++;
            String tariffName = "Generated " + i;
            if (tariffRepository.countByName(tariffName) > 0) {
                continue;
            }
            Tariff tariff = new Tariff();
            tariff.setName(tariffName);
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
            quantity--;
        }
    }

    @Override
    public List<TariffViewDto> getInactiveForCustomer(long customerId) {
        LocalDateTime now = LocalDateTime.now();
        return tariffRepository.findInactiveForCustomer(customerId, now)
                .stream()
                .map(tariff -> conversionService.convert(tariff, TariffViewDto.class))
                .collect(Collectors.toList());
    }

}
