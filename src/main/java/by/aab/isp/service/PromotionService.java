package by.aab.isp.service;

import java.util.List;

import by.aab.isp.dto.promotion.PromotionDto;
import by.aab.isp.entity.Promotion;

public interface PromotionService {

    List<PromotionDto> getAll(Pagination pagination);

    List<PromotionDto> getForHomepage();

    Promotion getById(Long id);

    Promotion save(PromotionDto promotion);

    void stop(long id);

    void generatePromotions(int quantity, boolean active);
}
