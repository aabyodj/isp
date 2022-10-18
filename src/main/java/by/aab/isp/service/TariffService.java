package by.aab.isp.service;

import java.util.List;

import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.dto.tariff.TariffDto;

public interface TariffService {
    
    List<ShowTariffDto> getAll();

    List<ShowTariffDto> getAll(Pagination pagination);

    List<ShowTariffDto> getForHomepage();

    List<ShowTariffDto> getInactiveForCustomer(long customerId);
    
    TariffDto getById(Long id);

    TariffDto save(TariffDto tariff);

    void generateTariffs(int quantity, boolean active);
}
