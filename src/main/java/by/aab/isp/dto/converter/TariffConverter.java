package by.aab.isp.dto.converter;

import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.stereotype.Component;

import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.entity.Tariff;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TariffConverter {

    private final FormatUtil formatUtil;
    private final Map<Tariff, ShowTariffDto> showTariffDtoCache = new WeakHashMap<>();

    public ShowTariffDto toShowTariffDto(Tariff tariff) {
        synchronized (showTariffDtoCache) {
            ShowTariffDto dto = showTariffDtoCache.get(tariff);
            if (null == dto) {
                dto = new ShowTariffDto();
                dto.setId(tariff.getId());
                dto.setName(tariff.getName());
                dto.setDescription(tariff.getDescription());
                dto.setBandwidth(formatUtil.formatBandwidth(tariff.getBandwidth()));
                dto.setIncludedTraffic(formatUtil.formatTraffic(tariff.getIncludedTraffic()));
                dto.setPrice(tariff.getPrice().toString());
                dto.setActive(tariff.isActive());
                showTariffDtoCache.put(tariff, dto);
            }
            return dto;
        }
    }
}
