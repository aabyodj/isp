package by.aab.isp.service.impl;

import by.aab.isp.config.Config;
import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.PromotionDao;
import by.aab.isp.entity.Promotion;
import by.aab.isp.service.PromotionService;
import by.aab.isp.service.ServiceException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static by.aab.isp.entity.Promotion.SORT_BY_ACTIVE_SINCE;
import static by.aab.isp.entity.Promotion.SORT_BY_ACTIVE_UNTIL;

public class PromotionServiceImpl implements PromotionService {

    private static final Comparator<Promotion> SORT_BY_SINCE_THEN_BY_UNTIL =
            SORT_BY_ACTIVE_SINCE.thenComparing(SORT_BY_ACTIVE_UNTIL);

    private static final int DEFAULT_PROMOTIONS_ON_HOMEPAGE = 3;

    private final PromotionDao promotionDao;
    private final Config config;

    public PromotionServiceImpl(PromotionDao promotionDao, Config config) {
        this.promotionDao = promotionDao;
        this.config = config;
    }

    @Override
    public Iterable<Promotion> getAll() {
        return sorted(promotionDao.findAll(), SORT_BY_SINCE_THEN_BY_UNTIL);
    }

    @Override
    public Iterable<Promotion> getForHomepage() {
        return promotionDao.findByActivePeriodContainsOrderBySinceReversedThenByUntil(
                LocalDateTime.now(),
                0,
                config.getInt("homepage.promotionsCount", DEFAULT_PROMOTIONS_ON_HOMEPAGE));
    }

    @Override
    public Promotion getById(Long id) {
        if (id != null) {
            return promotionDao.findById(id).orElseThrow();
        }
        Promotion promotion = new Promotion();
        promotion.setActiveSince(LocalDateTime.now());
        return promotion;
    }

    @Override
    public Promotion save(Promotion promotion) {
        promotion.setName(promotion.getName().strip());
        promotion.setDescription(promotion.getDescription().strip());
        try {
            if (promotion.getId() == null) {
                return promotionDao.save(promotion);
            } else {
                promotionDao.update(promotion);
                return promotion;
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void stop(long id) {
        Promotion promotion = promotionDao.findById(id).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        if (promotion.getActiveUntil() == null || promotion.getActiveUntil().isAfter(now)) {
            promotion.setActiveUntil(now);
            promotionDao.update(promotion);
        }
    }

    @Override
    public void generatePromotions(int quantity, boolean active) {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 1; i <= quantity; i++) {
            Promotion promotion = new Promotion();
            promotion.setName("Generated " + i);
            promotion.setDescription("Automatically generated promotion #" + i);
            promotion.setActiveSince(now);
            if (!active) {
                promotion.setActiveUntil(now);
            }
            promotionDao.save(promotion);
        }
    }

    private static Iterable<Promotion> sorted(Iterable<Promotion> promotions, Comparator<Promotion> comparator) {
        return StreamSupport
                .stream(promotions.spliterator(), true)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

}
