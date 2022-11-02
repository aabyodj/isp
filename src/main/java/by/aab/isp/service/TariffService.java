package by.aab.isp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import by.aab.isp.dto.tariff.TariffViewDto;
import by.aab.isp.dto.tariff.TariffEditDto;

public interface TariffService {
    
    List<TariffViewDto> getAll();

    Page<TariffViewDto> getAll(Pageable pageable);

    List<TariffViewDto> getActive();

    Page<TariffViewDto> getActive(Pageable pageable);

    List<TariffViewDto> getForHomepage();

    List<TariffViewDto> getInactiveForCustomer(long customerId);
    
    TariffEditDto getById(long id);

    TariffEditDto save(TariffEditDto tariff);

    void generateTariffs(int quantity, boolean active);
}
