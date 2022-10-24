package by.aab.isp.dto.converter;

import static by.aab.isp.Const.LDT_FOR_AGES;
import static by.aab.isp.Const.LDT_SINCE_AGES;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import by.aab.isp.dto.promotion.PromotionDto;
import by.aab.isp.entity.Promotion;

@Component
public class PromotionConverter {

    public Promotion toPromotion(PromotionDto dto) {
        Promotion promotion = new Promotion();
        promotion.setId(dto.getId());
        promotion.setName(dto.getName());
        promotion.setDescription(dto.getDescription());
        promotion.setActiveSince(dto.getActiveSince() != null ? dto.getActiveSince() : LDT_SINCE_AGES);
        promotion.setActiveUntil(dto.getActiveUntil() != null ? dto.getActiveUntil() : LDT_FOR_AGES);
        return promotion;
    }

    public PromotionDto toPromotionDto(Promotion promotion, LocalDateTime now) {
        PromotionDto dto = new PromotionDto();
        dto.setId(promotion.getId());
        dto.setName(promotion.getName());
        dto.setDescription(promotion.getDescription());
        LocalDateTime activeSince = promotion.getActiveSince();
        dto.setActiveSince(activeSince.isAfter(LDT_SINCE_AGES) ? activeSince : null);
        LocalDateTime activeUntil = promotion.getActiveUntil();
        dto.setActiveUntil(activeUntil.isBefore(LDT_FOR_AGES) ? activeUntil : null);
        dto.setActive(!activeSince.isAfter(now) && !activeUntil.isBefore(now));
        return dto;
    }

    public PromotionDto toPromotionDto(Promotion promotion) {
        return toPromotionDto(promotion, LocalDateTime.now());
    }

}
