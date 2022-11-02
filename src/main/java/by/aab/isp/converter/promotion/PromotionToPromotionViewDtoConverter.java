package by.aab.isp.converter.promotion;

import static by.aab.isp.Const.LDT_FOR_AGES;
import static by.aab.isp.Const.LDT_SINCE_AGES;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.dto.promotion.PromotionViewDto;
import by.aab.isp.entity.Promotion;
import by.aab.isp.service.Now;

@Component
public class PromotionToPromotionViewDtoConverter implements Converter<Promotion, PromotionViewDto> {

    @Autowired
    private Now now;

    @Override
    public PromotionViewDto convert(Promotion promotion) {
        LocalDateTime activeSince = promotion.getActiveSince();
        LocalDateTime activeUntil = promotion.getActiveUntil();
        return PromotionViewDto.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .description(promotion.getDescription())
                .activeSince(activeSince.isAfter(LDT_SINCE_AGES) ? activeSince
                                                                 : null)
                .activeUntil(activeUntil.isBefore(LDT_FOR_AGES) ? activeUntil
                                                                : null)
                .active(!activeSince.isAfter(now.getLocalDateTime()) && !activeUntil.isBefore(now.getLocalDateTime()))
                .build();
    }

}
