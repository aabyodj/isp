package by.aab.isp.service.converter.promotion;

import static by.aab.isp.Const.LDT_FOR_AGES;
import static by.aab.isp.Const.LDT_SINCE_AGES;

import java.time.LocalDateTime;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.repository.entity.Promotion;
import by.aab.isp.service.Now;
import by.aab.isp.service.dto.promotion.PromotionViewDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PromotionToPromotionViewDtoConverter implements Converter<Promotion, PromotionViewDto> {

    private final Now now;

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
