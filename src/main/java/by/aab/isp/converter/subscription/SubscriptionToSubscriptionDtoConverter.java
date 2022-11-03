package by.aab.isp.converter.subscription;

import static by.aab.isp.Const.LDT_FOR_AGES;
import static by.aab.isp.Const.TRAFFIC_UNLIMITED;

import java.time.LocalDateTime;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.converter.tariff.TariffToTariffViewDtoConverter;
import by.aab.isp.dto.converter.FormatUtil;
import by.aab.isp.dto.subscription.SubscriptionViewDto;
import by.aab.isp.entity.Subscription;
import by.aab.isp.service.Now;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionToSubscriptionDtoConverter implements Converter<Subscription, SubscriptionViewDto> {

    private final FormatUtil formatUtil;
    private final TariffToTariffViewDtoConverter tariffConverter;

    private Now now;

    public SubscriptionViewDto convert(Subscription entity) {
        SubscriptionViewDto dto = new SubscriptionViewDto();
        dto.setId(entity.getId());
        dto.setTariff(tariffConverter.convert(entity.getTariff()));
        dto.setPrice(entity.getPrice());
        dto.setTrafficConsumed(formatUtil.formatTraffic(entity.getTrafficConsumed()));
        long trafficPerPeriod = entity.getTrafficPerPeriod();
        long trafficLeft = (trafficPerPeriod >= 0 || trafficPerPeriod < TRAFFIC_UNLIMITED)
                ? trafficPerPeriod - entity.getTrafficConsumed()
                : TRAFFIC_UNLIMITED;
        dto.setTrafficLeft(formatUtil.formatTraffic(trafficLeft));
        dto.setTrafficPerPeriod(formatUtil.formatTraffic(entity.getTrafficPerPeriod()));
        dto.setActiveSince(entity.getActiveSince().toString());
        dto.setActiveUntil(formatActiveUntil(entity.getActiveUntil()));
        dto.setActive(!entity.getActiveSince().isAfter(now.getLocalDateTime()) && !entity.getActiveUntil().isBefore(now.getLocalDateTime()));
        return dto;
    }

    private String formatActiveUntil(LocalDateTime activeUntil) {
        if (!activeUntil.isBefore(LDT_FOR_AGES)) {
            return "until cancelled";
        }
        return activeUntil.toString();
    }

}
