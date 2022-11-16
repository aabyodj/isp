package by.aab.isp.service.converter.promotion;

import static by.aab.isp.Const.LDT_FOR_AGES;
import static by.aab.isp.Const.LDT_SINCE_AGES;

import java.time.LocalDateTime;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.repository.entity.Promotion;
import by.aab.isp.service.dto.promotion.PromotionEditDto;

@Component
public class PromotionToPromotionEditDtoConverter implements Converter<Promotion, PromotionEditDto> {

    @Override
    public PromotionEditDto convert(Promotion promotion) {
        LocalDateTime activeSince = promotion.getActiveSince();
        LocalDateTime activeUntil = promotion.getActiveUntil();
        return PromotionEditDto.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .description(promotion.getDescription())
                .activeSince(activeSince.isAfter(LDT_SINCE_AGES) ? activeSince.toLocalDate()
                                                                 : null)
                .activeUntil(activeUntil.isBefore(LDT_FOR_AGES) ? activeUntil.toLocalDate()
                                                                : null)
                .build();
    }

}
