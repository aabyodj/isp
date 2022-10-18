package by.aab.isp.dto.converter;

import static by.aab.isp.Const.BANDWIDTH_UNLIMITED;
import static by.aab.isp.Const.TRAFFIC_UNLIMITED;

import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.stereotype.Component;

import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.dto.tariff.TariffDto;
import by.aab.isp.entity.Tariff;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TariffConverter {

    private final FormatUtil formatUtil;
    private final Map<Tariff, ShowTariffDto> showTariffDtoCache = new WeakHashMap<>();

    public Tariff toTariff(TariffDto dto) {
        Tariff tariff = new Tariff();
        tariff.setId(dto.getId());
        tariff.setName(dto.getName());
        tariff.setDescription(dto.getDescription());
        tariff.setBandwidth(dto.getBandwidth() != null ? dto.getBandwidth() : BANDWIDTH_UNLIMITED);
        tariff.setIncludedTraffic(dto.getIncludedTraffic() != null ? dto.getIncludedTraffic() : TRAFFIC_UNLIMITED);
        tariff.setPrice(dto.getPrice());
        tariff.setActive(dto.isActive());
        return tariff;
    }

    public TariffDto toDto(Tariff tariff) {
        TariffDto dto = new TariffDto();
        dto.setId(tariff.getId());
        dto.setName(tariff.getName());
        dto.setDescription(tariff.getDescription());
        int bandwidth = tariff.getBandwidth();
        dto.setBandwidth(bandwidth < BANDWIDTH_UNLIMITED && bandwidth > 0 ? bandwidth : null);
        long includedTraffic = tariff.getIncludedTraffic();
        dto.setIncludedTraffic(includedTraffic < TRAFFIC_UNLIMITED && includedTraffic > 0 ? includedTraffic : null);
        dto.setPrice(tariff.getPrice());
        dto.setActive(tariff.isActive());
        return dto;
    }

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
