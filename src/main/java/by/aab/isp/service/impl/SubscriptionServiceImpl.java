package by.aab.isp.service.impl;

import by.aab.isp.dao.SubscriptionDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;
import by.aab.isp.entity.Tariff;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;

import java.time.Instant;

public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionDao subscriptionDao;
    private final TariffService tariffService;

    public SubscriptionServiceImpl(SubscriptionDao subscriptionDao, TariffService tariffService) {
        this.subscriptionDao = subscriptionDao;
        this.tariffService = tariffService;
    }

    @Override
    public Iterable<Subscription> getActiveSubscriptions(Customer customer) {
        return subscriptionDao.findByCustomerIdAndActivePeriodContains(customer.getId(), Instant.now());
    }

    @Override
    public void setOneTariffForCustomer(long tariffId, Customer customer) {
        Iterable<Subscription> subscriptions = getActiveSubscriptions(customer);
        boolean alreadySet = false;
        Instant now = Instant.now();
        for (Subscription subscription : subscriptions) {
            if (subscription.getTariff().getId() == tariffId) {
                alreadySet = true;
            } else {
                subscription.setActiveUntil(now);
                subscriptionDao.update(subscription);
            }
        }
        if (!alreadySet && tariffId != 0) {
            Tariff tariff = tariffService.getById(tariffId);
            Subscription subscription = new Subscription();
            subscription.setCustomer(customer);
            subscription.setTariff(tariff);
            subscription.setPrice(tariff.getPrice());
            subscription.setActiveSince(now);
            subscription.setActiveUntil(null);
            subscriptionDao.save(subscription);
        }
    }
}
