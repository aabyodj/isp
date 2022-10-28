package by.aab.isp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.dto.tariff.TariffDto;

public interface TariffService {
    
    List<ShowTariffDto> getAll();

    Page<ShowTariffDto> getAll(Pageable pageable);

    List<ShowTariffDto> getActive();

    Page<ShowTariffDto> getActive(Pageable pageable);

    List<ShowTariffDto> getForHomepage();

    List<ShowTariffDto> getInactiveForCustomer(long customerId);
    
    TariffDto getById(Long id);

    TariffDto save(TariffDto tariff);

    void generateTariffs(int quantity, boolean active);
}
