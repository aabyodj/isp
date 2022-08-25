package by.aab.isp.service;

import by.aab.isp.entity.Promotion;

public interface PromotionService {

    Iterable<Promotion> getAll(Pagination pagination);

    Iterable<Promotion> getForHomepage();

    Promotion getById(Long id);

    Promotion save(Promotion promotion);

    void stop(long id);

    void generatePromotions(int quantity, boolean active);
}
