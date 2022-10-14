package by.aab.isp.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import by.aab.isp.aspect.AutoLogged;
import by.aab.isp.config.ConfigManager;
import by.aab.isp.entity.Promotion;
import by.aab.isp.repository.OrderOffsetLimit;
import by.aab.isp.repository.PromotionRepository;
import by.aab.isp.service.Pagination;
import by.aab.isp.service.PromotionService;

@Service("promotionService")
public class PromotionServiceImpl implements PromotionService {

    private static final int DEFAULT_PROMOTIONS_ON_HOMEPAGE = 3;

    private final PromotionRepository promotionRepository;
    private final ConfigManager config;

    public PromotionServiceImpl(PromotionRepository promotionRepository, ConfigManager config) {
        this.promotionRepository = promotionRepository;
        this.config = config;
    }

    private static final List<OrderOffsetLimit.Order> ORDER_BY_SINCE_THEN_BY_UNTIL = List.of(
            new OrderOffsetLimit.Order("activeSince", true),
            new OrderOffsetLimit.Order("activeUntil", true)
    );

    @AutoLogged
    @Override
    public Iterable<Promotion> getAll(Pagination pagination) {
        long count = promotionRepository.count();
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
            return promotionRepository.findAll(orderOffsetLimit);
        } else {
            return List.of();
        }
    }

    private static final List<OrderOffsetLimit.Order> ORDER_BY_SINCE_REVERSED_THEN_BY_UNTIL = List.of(
            new OrderOffsetLimit.Order("activeSince", false),
            new OrderOffsetLimit.Order("activeUntil", true)
    );

    @AutoLogged
    @Override
    public Iterable<Promotion> getForHomepage() {
        OrderOffsetLimit orderOffsetLimit = new OrderOffsetLimit();
        orderOffsetLimit.setOrderList(ORDER_BY_SINCE_REVERSED_THEN_BY_UNTIL);
        orderOffsetLimit.setLimit(config.getInt("homepage.promotionsCount", DEFAULT_PROMOTIONS_ON_HOMEPAGE));
        return promotionRepository.findByActivePeriodContains(LocalDateTime.now(), orderOffsetLimit);
    }

    @AutoLogged
    @Override
    public Promotion getById(Long id) {
        if (id != null) {
            return promotionRepository.findById(id).orElseThrow();
        }
        Promotion promotion = new Promotion();
        promotion.setActiveSince(LocalDateTime.now());
        return promotion;
    }

    @AutoLogged
    @Override
    public Promotion save(Promotion promotion) {
        promotion.setName(promotion.getName().strip());
        promotion.setDescription(promotion.getDescription().strip());
        if (promotion.getId() == null) {
            return promotionRepository.save(promotion);
        } else {
            promotionRepository.update(promotion);
            return promotion;
        }
    }

    @AutoLogged
    @Override
    public void stop(long id) {
        Promotion promotion = promotionRepository.findById(id).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        if (promotion.getActiveUntil() == null || promotion.getActiveUntil().isAfter(now)) {
            promotion.setActiveUntil(now);
            promotionRepository.update(promotion);
        }
    }

    @AutoLogged
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
            promotionRepository.save(promotion);
        }
    }

}
