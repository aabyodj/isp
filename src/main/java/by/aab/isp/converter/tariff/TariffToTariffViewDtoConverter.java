package by.aab.isp.converter.tariff;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.converter.FormatUtil;
import by.aab.isp.dto.tariff.TariffViewDto;
import by.aab.isp.repository.entity.Tariff;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TariffToTariffViewDtoConverter implements Converter<Tariff, TariffViewDto> {

    private final FormatUtil formatUtil;

    @Override
    public TariffViewDto convert(Tariff entity) {
        return TariffViewDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .bandwidth(formatUtil.formatBandwidth(entity.getBandwidth()))
                .includedTraffic(formatUtil.formatTraffic(entity.getIncludedTraffic()))
                .price(entity.getPrice().toString())
                .active(entity.isActive())
                .build();
    }

}
