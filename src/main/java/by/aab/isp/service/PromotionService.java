package by.aab.isp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import by.aab.isp.dto.promotion.PromotionDto;

public interface PromotionService {

    Page<PromotionDto> getAll(Pageable pageable);

    Page<PromotionDto> getActive(Pageable pageable);

    List<PromotionDto> getForHomepage();

    PromotionDto getById(Long id);

    PromotionDto save(PromotionDto promotion);

    void stop(long id);

    void generatePromotions(int quantity, boolean active);
}
