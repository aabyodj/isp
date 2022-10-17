package by.aab.isp.service;

import java.util.List;

import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.entity.Tariff;

public interface TariffService {
    
    List<ShowTariffDto> getAll();

    List<ShowTariffDto> getAll(Pagination pagination);

    List<ShowTariffDto> getForHomepage();

    List<ShowTariffDto> getInactiveForCustomer(long customerId);
    
    Tariff getById(Long id);

    Tariff save(Tariff tariff);

    void generateTariffs(int quantity, boolean active);
}
