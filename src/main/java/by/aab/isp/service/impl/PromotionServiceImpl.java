package by.aab.isp.service.impl;

import static by.aab.isp.Const.LDT_FOR_AGES;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import by.aab.isp.aspect.AutoLogged;
import by.aab.isp.config.HomepageProperties;
import by.aab.isp.dto.converter.PromotionConverter;
import by.aab.isp.dto.promotion.PromotionDto;
import by.aab.isp.entity.Promotion;
import by.aab.isp.repository.PromotionRepository;
import by.aab.isp.service.PromotionService;
import lombok.RequiredArgsConstructor;

@Service("promotionService")
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionConverter promotionConverter;
    private final HomepageProperties homepageProperties;

    @AutoLogged
    @Override
    public Page<PromotionDto> getAll(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findAll(pageable).map(promotion -> promotionConverter.toPromotionDto(promotion, now));
    }

    @AutoLogged
    @Override
    public Page<PromotionDto> getActive(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findByActivePeriodContains(now, pageable).map(promotion -> promotionConverter.toPromotionDto(promotion, now));
    }

    private static final Sort ORDER_BY_SINCE_REVERSED_THEN_BY_UNTIL = Sort.by("activeSince").descending().and(Sort.by("activeUntil"));

    @AutoLogged
    @Override
    public List<PromotionDto> getForHomepage() {
        int pageSize = homepageProperties.getPromotionsCount();
        PageRequest request = PageRequest.of(0, pageSize, ORDER_BY_SINCE_REVERSED_THEN_BY_UNTIL);
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findByActivePeriodContains(LocalDateTime.now(), request)
                .map(promotion -> promotionConverter.toPromotionDto(promotion, now))
                .toList();
    }

    @AutoLogged
    @Override
    public PromotionDto getById(Long id) {
        if (id != null) {
            return promotionConverter.toPromotionDto(promotionRepository.findById(id).orElseThrow()) ;
        }
        PromotionDto promotion = new PromotionDto();
        promotion.setActiveSince(LocalDateTime.now());
        return promotion;
    }

    @AutoLogged
    @Override
    public PromotionDto save(PromotionDto dto) {
        dto.setName(dto.getName().strip());
        dto.setDescription(dto.getDescription().strip());
        Promotion promotion = promotionConverter.toPromotion(dto);
        promotion = promotionRepository.save(promotion);
        dto.setId(promotion.getId());
        return dto;
    }

    @AutoLogged
    @Transactional
    @Override
    public void stop(long id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        if (promotion.getActiveUntil() == null || promotion.getActiveUntil().isAfter(now)) {
            promotion.setActiveUntil(now);
            promotionRepository.save(promotion);
        }
    }

    @AutoLogged
    @Override
    public void generatePromotions(int quantity, boolean active) {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        for (int i = 1; i <= quantity; i++) {
            Promotion promotion = new Promotion();
            promotion.setName("Generated " + i);
            promotion.setDescription("Automatically generated promotion #" + i);
            promotion.setActiveSince(today);
            promotion.setActiveUntil(active ? LDT_FOR_AGES : today);
            promotionRepository.save(promotion);
        }
    }

}
