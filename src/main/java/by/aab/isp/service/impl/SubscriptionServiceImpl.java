package by.aab.isp.service.impl;

import by.aab.isp.dao.OrderOffsetLimit;
import by.aab.isp.dao.SubscriptionDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;
import by.aab.isp.entity.Tariff;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service("subscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionDao subscriptionDao;
    private final TariffService tariffService;

    public SubscriptionServiceImpl(SubscriptionDao subscriptionDao, TariffService tariffService) {
        this.subscriptionDao = subscriptionDao;
        this.tariffService = tariffService;
    }

    private static final List<OrderOffsetLimit.Order> ORDER_BY_SINCE_THEN_BY_UNTIL = List.of(
            new OrderOffsetLimit.Order("activeSince", true),
            new OrderOffsetLimit.Order("activeUntil", true)
    );

    @Override
    public Iterable<Subscription> getByCustomer(Customer customer) {
        OrderOffsetLimit orderOffsetLimit = new OrderOffsetLimit();
        orderOffsetLimit.setOrderList(ORDER_BY_SINCE_THEN_BY_UNTIL);
        return subscriptionDao.findByCustomerId(customer.getId(), orderOffsetLimit);
    }

    @Override
    public Iterable<Subscription> getActiveSubscriptions(Customer customer) {
        return subscriptionDao.findByCustomerIdAndActivePeriodContains(customer.getId(), LocalDateTime.now());
    }

    @Override
    public void subscribe(Customer customer, long tariffId) {
        setOneTariffForCustomer(customer, tariffId);    //TODO: add multiply active subscriptions feature
    }

    @Override
    public void setOneTariffForCustomer(Customer customer, long tariffId) {
        Iterable<Subscription> subscriptions = getActiveSubscriptions(customer);
        boolean alreadySet = false;
        LocalDateTime now = LocalDateTime.now();
        for (Subscription subscription : subscriptions) {
            if (subscription.getTariff().getId() == tariffId) {
                alreadySet = true;
            } else {
                subscription.setActiveUntil(now);
                subscriptionDao.update(subscription);
            }
        }
        if (!alreadySet) {
            Tariff tariff = tariffService.getById(tariffId);
            Subscription subscription = new Subscription();
            subscription.setCustomer(customer);
            subscription.setTariff(tariff);
            subscription.setPrice(tariff.getPrice());
            subscription.setTrafficPerPeriod(tariff.getIncludedTraffic());
            subscription.setActiveSince(now);
            subscription.setActiveUntil(null);
            subscriptionDao.save(subscription);
        }
    }

    @Override
    public void cancelSubscription(Customer customer, long subscriptionId) {
        Subscription subscription = subscriptionDao.findById(subscriptionId).orElseThrow();
        if ((long) customer.getId() != subscription.getCustomer().getId()) {
            throw new ServiceException("The subscription does not belong to the customer");
        }
        subscription.setActiveUntil(LocalDateTime.now());
        subscriptionDao.update(subscription);
    }
}
