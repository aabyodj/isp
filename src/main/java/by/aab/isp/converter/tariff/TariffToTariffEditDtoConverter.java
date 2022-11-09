package by.aab.isp.converter.tariff;

import static by.aab.isp.Const.BANDWIDTH_UNLIMITED;
import static by.aab.isp.Const.TRAFFIC_UNLIMITED;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.repository.entity.Tariff;
import by.aab.isp.service.dto.tariff.TariffEditDto;

@Component
public class TariffToTariffEditDtoConverter implements Converter<Tariff, TariffEditDto> {

    public static final int BYTES_PER_MEGABYTE = 1024 * 1024;

    @Override
    public TariffEditDto convert(Tariff entity) {
        return TariffEditDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .bandwidth(entity.getBandwidth() > 0 && entity.getBandwidth() < BANDWIDTH_UNLIMITED ? entity.getBandwidth()
                                                                                                    : 0)
                .includedTraffic(entity.getIncludedTraffic() > 0 && entity.getIncludedTraffic() < TRAFFIC_UNLIMITED ? entity.getIncludedTraffic() / BYTES_PER_MEGABYTE
                                                                                                                    : 0)
                .price(entity.getPrice())
                .active(entity.isActive())
                .build();
    }

}
