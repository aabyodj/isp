package by.aab.isp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import by.aab.isp.dto.promotion.PromotionEditDto;
import by.aab.isp.dto.promotion.PromotionViewDto;

public interface PromotionService {

    Page<PromotionViewDto> getAll(Pageable pageable);

    Page<PromotionViewDto> getActive(Pageable pageable);

    PromotionEditDto getById(Long id);

    PromotionEditDto save(PromotionEditDto promotion);

    void stop(long id);

    void generatePromotions(int quantity, boolean active);
}
