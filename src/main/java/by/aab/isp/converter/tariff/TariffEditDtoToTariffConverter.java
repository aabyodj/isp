package by.aab.isp.converter.tariff;

import static by.aab.isp.Const.BANDWIDTH_UNLIMITED;
import static by.aab.isp.Const.TRAFFIC_UNLIMITED;
import static by.aab.isp.converter.tariff.TariffToTariffEditDtoConverter.BYTES_PER_MEGABYTE;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.dto.tariff.TariffEditDto;
import by.aab.isp.entity.Tariff;

@Component
public class TariffEditDtoToTariffConverter implements Converter<TariffEditDto, Tariff> {

    @Override
    public Tariff convert(TariffEditDto dto) {
        return Tariff.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .bandwidth(dto.getBandwidth() > 0 ? dto.getBandwidth() : BANDWIDTH_UNLIMITED)
                .includedTraffic(dto.getIncludedTraffic() > 0 ? dto.getIncludedTraffic() * BYTES_PER_MEGABYTE : TRAFFIC_UNLIMITED)
                .price(dto.getPrice())
                .active(dto.isActive())
                .build();
    }

}
