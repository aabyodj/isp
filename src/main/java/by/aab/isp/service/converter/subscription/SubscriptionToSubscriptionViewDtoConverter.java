package by.aab.isp.service.converter.subscription;

import static by.aab.isp.Const.LDT_FOR_AGES;
import static by.aab.isp.Const.TRAFFIC_UNLIMITED;

import java.time.LocalDateTime;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.repository.entity.Subscription;
import by.aab.isp.service.Now;
import by.aab.isp.service.converter.FormatUtil;
import by.aab.isp.service.converter.tariff.TariffToTariffViewDtoConverter;
import by.aab.isp.service.dto.subscription.SubscriptionViewDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionToSubscriptionViewDtoConverter implements Converter<Subscription, SubscriptionViewDto> {

    private final FormatUtil formatUtil;
    private final TariffToTariffViewDtoConverter tariffConverter;
    private final MessageSource messageSource;
    private final Now now;

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
            return messageSource.getMessage("msg.subscription.table.until.for-ages", null, LocaleContextHolder.getLocale());
        }
        return activeUntil.toString();
    }

}
