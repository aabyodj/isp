package by.aab.isp.service.impl;

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

    private static final Comparator<Promotion> SORT_BY_SINCE_REVERSED_THEN_BY_UNTIL =
            SORT_BY_ACTIVE_SINCE.reversed().thenComparing(SORT_BY_ACTIVE_UNTIL);

    private final PromotionDao promotionDao;

    public PromotionServiceImpl(PromotionDao promotionDao) {
        this.promotionDao = promotionDao;
    }

    @Override
    public Iterable<Promotion> getAll() {
        return sorted(promotionDao.findAll(), SORT_BY_SINCE_THEN_BY_UNTIL);
    }

    @Override
    public Iterable<Promotion> getForHomepage() {
        return sorted(promotionDao.findByActivePeriodContains(LocalDateTime.now()), SORT_BY_SINCE_REVERSED_THEN_BY_UNTIL);
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

    private static Iterable<Promotion> sorted(Iterable<Promotion> promotions, Comparator<Promotion> comparator) {
        return StreamSupport
                .stream(promotions.spliterator(), true)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

}
