package by.aab.isp.service.impl;

import by.aab.isp.config.Config;
import by.aab.isp.dao.DaoException;
import by.aab.isp.dao.OrderOffsetLimit;
import by.aab.isp.dao.PromotionDao;
import by.aab.isp.entity.Promotion;
import by.aab.isp.service.Pagination;
import by.aab.isp.service.PromotionService;
import by.aab.isp.service.ServiceException;

import java.time.LocalDateTime;
import java.util.List;

public class PromotionServiceImpl implements PromotionService {

    private static final int DEFAULT_PROMOTIONS_ON_HOMEPAGE = 3;

    private final PromotionDao promotionDao;
    private final Config config;

    public PromotionServiceImpl(PromotionDao promotionDao, Config config) {
        this.promotionDao = promotionDao;
        this.config = config;
    }

    private static final List<OrderOffsetLimit.Order> ORDER_BY_SINCE_THEN_BY_UNTIL = List.of(
            new OrderOffsetLimit.Order("activeSince", true),
            new OrderOffsetLimit.Order("activeUntil", true)
    );

    @Override
    public Iterable<Promotion> getAll(Pagination pagination) {
        long count = promotionDao.count();
        pagination.setTotalItemsCount(count);
        long offset = pagination.getOffset();
        if (offset >= count) {
            pagination.setPageNumber(pagination.getLastPageNumber());
        } else {
            pagination.setOffset(Long.max(0, offset));
        }
        if (count > 0) {
            OrderOffsetLimit orderOffsetLimit = new OrderOffsetLimit();
            orderOffsetLimit.setOrderList(ORDER_BY_SINCE_THEN_BY_UNTIL);
            orderOffsetLimit.setOffset(pagination.getOffset());
            orderOffsetLimit.setLimit(pagination.getPageSize());
            return promotionDao.findAll(orderOffsetLimit);
        } else {
            return List.of();
        }
    }

    private static final List<OrderOffsetLimit.Order> ORDER_BY_SINCE_REVERSED_THEN_BY_UNTIL = List.of(
            new OrderOffsetLimit.Order("activeSince", false),
            new OrderOffsetLimit.Order("activeUntil", true)
    );

    @Override
    public Iterable<Promotion> getForHomepage() {
        OrderOffsetLimit orderOffsetLimit = new OrderOffsetLimit();
        orderOffsetLimit.setOrderList(ORDER_BY_SINCE_REVERSED_THEN_BY_UNTIL);
        orderOffsetLimit.setLimit(config.getInt("homepage.promotionsCount", DEFAULT_PROMOTIONS_ON_HOMEPAGE));
        return promotionDao.findByActivePeriodContains(LocalDateTime.now(), orderOffsetLimit);
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

}
