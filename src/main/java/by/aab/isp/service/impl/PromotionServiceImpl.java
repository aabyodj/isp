package by.aab.isp.service.impl;

import static by.aab.isp.Const.LDT_FOR_AGES;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import by.aab.isp.dto.promotion.PromotionEditDto;
import by.aab.isp.dto.promotion.PromotionViewDto;
import by.aab.isp.repository.PromotionRepository;
import by.aab.isp.repository.entity.Promotion;
import by.aab.isp.service.NotFoundException;
import by.aab.isp.service.Now;
import by.aab.isp.service.PromotionService;
import by.aab.isp.service.log.Autologged;
import lombok.RequiredArgsConstructor;

@Service("promotionService")
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final ConversionService conversionService;

    @Autowired
    private Now now;

    @Autologged
    @Override
    public Page<PromotionViewDto> getAll(Pageable pageable) {
        return promotionRepository.findAll(pageable)
                .map(promotion -> conversionService.convert(promotion, PromotionViewDto.class));
    }

    @Autologged
    @Override
    public Page<PromotionViewDto> getActive(Pageable pageable) {
        return promotionRepository.findByActivePeriodContains(now.getLocalDateTime(), pageable)
                .map(promotion -> conversionService.convert(promotion, PromotionViewDto.class));
    }

    @Autologged
    @Override
    public PromotionEditDto getById(Long id) {
        return promotionRepository.findById(id)
                .map(promotion -> conversionService.convert(promotion, PromotionEditDto.class))
                .orElseThrow(NotFoundException::new);
    }

    @Autologged
    @Override
    public PromotionEditDto save(PromotionEditDto dto) {
        dto.setName(dto.getName().strip());
        dto.setDescription(dto.getDescription().strip());
        Promotion promotion = conversionService.convert(dto, Promotion.class);
        promotion = promotionRepository.save(promotion);
        dto.setId(promotion.getId());
        return dto;
    }

    @Autologged
    @Transactional
    @Override
    public void stop(long id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow();
        if (promotion.getActiveUntil().isAfter(now.getLocalDateTime())) {
            promotion.setActiveUntil(now.getLocalDateTime());
            promotionRepository.save(promotion);
        }
    }

    @Autologged
    @Transactional
    @Override
    public void generatePromotions(int quantity, boolean active) {
        LocalDateTime today = now.getLocalDate().atStartOfDay();
        int i = 0;
        while (quantity > 0) {
            i++;
            String promotionName = "Generated " + i;
            if (promotionRepository.countByName(promotionName) > 0) {
                continue;
            }
            Promotion promotion = new Promotion();
            promotion.setName(promotionName);
            promotion.setDescription("Automatically generated promotion #" + i);
            promotion.setActiveSince(today);
            promotion.setActiveUntil(active ? LDT_FOR_AGES : today);
            promotionRepository.save(promotion);
            quantity--;
        }
    }

}
