package by.aab.isp.service.impl;

import static by.aab.isp.Const.LDT_FOR_AGES;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import by.aab.isp.aspect.AutoLogged;
import by.aab.isp.config.HomepageProperties;
import by.aab.isp.dto.promotion.PromotionEditDto;
import by.aab.isp.dto.promotion.PromotionViewDto;
import by.aab.isp.entity.Promotion;
import by.aab.isp.repository.PromotionRepository;
import by.aab.isp.service.NotFoundException;
import by.aab.isp.service.Now;
import by.aab.isp.service.PromotionService;
import lombok.RequiredArgsConstructor;

@Service("promotionService")
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final ConversionService conversionService;
    private final HomepageProperties homepageProperties;

    @Autowired
    private Now now;

    @AutoLogged
    @Override
    public Page<PromotionViewDto> getAll(Pageable pageable) {
        return promotionRepository.findAll(pageable)
                .map(promotion -> conversionService.convert(promotion, PromotionViewDto.class));
    }

    @AutoLogged
    @Override
    public Page<PromotionViewDto> getActive(Pageable pageable) {
        return promotionRepository.findByActivePeriodContains(now.getLocalDateTime(), pageable)
                .map(promotion -> conversionService.convert(promotion, PromotionViewDto.class));
    }

    private static final Sort ORDER_BY_SINCE_REVERSED_THEN_BY_UNTIL = Sort.by("activeSince").descending().and(Sort.by("activeUntil"));

    @AutoLogged
    @Override
    public List<PromotionViewDto> getForHomepage() {
        int pageSize = homepageProperties.getPromotionsCount();
        PageRequest request = PageRequest.of(0, pageSize, ORDER_BY_SINCE_REVERSED_THEN_BY_UNTIL);
        return promotionRepository.findByActivePeriodContains(now.getLocalDateTime(), request)
                .map(promotion -> conversionService.convert(promotion, PromotionViewDto.class))
                .toList();
    }

    @AutoLogged
    @Override
    public PromotionEditDto getById(Long id) {
        return promotionRepository.findById(id)
                .map(promotion -> conversionService.convert(promotion, PromotionEditDto.class))
                .orElseThrow(NotFoundException::new);
    }

    @AutoLogged
    @Override
    public PromotionEditDto save(PromotionEditDto dto) {
        dto.setName(dto.getName().strip());
        dto.setDescription(dto.getDescription().strip());
        Promotion promotion = conversionService.convert(dto, Promotion.class);
        promotion = promotionRepository.save(promotion);
        dto.setId(promotion.getId());
        return dto;
    }

    @AutoLogged
    @Transactional
    @Override
    public void stop(long id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow();
        if (promotion.getActiveUntil().isAfter(now.getLocalDateTime())) {
            promotion.setActiveUntil(now.getLocalDateTime());
            promotionRepository.save(promotion);
        }
    }

    @AutoLogged
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
