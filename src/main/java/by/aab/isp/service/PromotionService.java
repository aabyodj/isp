package by.aab.isp.service;

import by.aab.isp.entity.Promotion;

public interface PromotionService {

    Iterable<Promotion> getAll();

    Promotion getById(long id);

    Promotion save(Promotion promotion);
}
