package by.aab.isp.service.converter.promotion;

import static by.aab.isp.Const.LDT_FOR_AGES;
import static by.aab.isp.Const.LDT_SINCE_AGES;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import by.aab.isp.repository.entity.Promotion;
import by.aab.isp.service.dto.promotion.PromotionEditDto;

@Component
public class PromotionEditDtoToPromotionConverter implements Converter<PromotionEditDto, Promotion> {

    @Override
    public Promotion convert(PromotionEditDto dto) {
        return Promotion.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .activeSince(dto.getActiveSince() != null ? dto.getActiveSince().atStartOfDay()
                                                          : LDT_SINCE_AGES)
                .activeUntil(dto.getActiveUntil() != null ? dto.getActiveUntil().plusDays(1).atStartOfDay().minusNanos(1000)
                                                          : LDT_FOR_AGES)
                .build();
    }

}
