package by.aab.isp.dto.converter;

import static by.aab.isp.Const.LDT_FOR_AGES;
import static by.aab.isp.Const.TRAFFIC_UNLIMITED;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import by.aab.isp.dto.subscription.SubscriptionDto;
import by.aab.isp.entity.Subscription;
import by.aab.isp.web.FormatUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionConverter {

    private final TariffConverter tariffConverter;
    private final FormatUtil formatUtil;

    public SubscriptionDto toDto(Subscription subscription, LocalDateTime now) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(subscription.getId());
        dto.setTariff(tariffConverter.toShowTariffDto(subscription.getTariff()));
        dto.setPrice(subscription.getPrice());
        dto.setTrafficConsumed(formatUtil.formatTraffic(subscription.getTrafficConsumed()));
        long trafficPerPeriod = subscription.getTrafficPerPeriod();
        long trafficLeft = (trafficPerPeriod >= 0 || trafficPerPeriod < TRAFFIC_UNLIMITED) 
                ? trafficPerPeriod - subscription.getTrafficConsumed()
                : TRAFFIC_UNLIMITED;
        dto.setTrafficLeft(formatUtil.formatTraffic(trafficLeft));
        dto.setTrafficPerPeriod(formatUtil.formatTraffic(subscription.getTrafficPerPeriod()));
        dto.setActiveSince(subscription.getActiveSince().toString());
        dto.setActiveUntil(formatActiveUntil(subscription.getActiveUntil()));
        dto.setActive(!subscription.getActiveSince().isAfter(now) && !subscription.getActiveUntil().isBefore(now));
        return dto;
    }

    public SubscriptionDto toDto(Subscription subscription) {
        return toDto(subscription, LocalDateTime.now());
    }

    private String formatActiveUntil(LocalDateTime activeUntil) {
        if (!activeUntil.isBefore(LDT_FOR_AGES)) {
            return "until cancelled";
        }
        return activeUntil.toString();
    }

}
