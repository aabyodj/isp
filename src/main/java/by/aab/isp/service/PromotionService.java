package by.aab.isp.service;

import by.aab.isp.entity.Promotion;

public interface PromotionService {

    Iterable<Promotion> getAll();

    Iterable<Promotion> getForHomepage();

    Promotion getById(long id);

    Promotion save(Promotion promotion);

    void stop(long id);
}
