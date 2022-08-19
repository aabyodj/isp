package by.aab.isp.service.impl;

import by.aab.isp.dao.SubscriptionDao;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;
import by.aab.isp.entity.Tariff;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;

import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static by.aab.isp.entity.Subscription.SORT_BY_ACTIVE_SINCE;
import static by.aab.isp.entity.Subscription.SORT_BY_ACTIVE_UNTIL;

public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Comparator<Subscription> SORT_BY_SINCE_THEN_BY_UNTIL =
            SORT_BY_ACTIVE_SINCE.thenComparing(SORT_BY_ACTIVE_UNTIL);

    private final SubscriptionDao subscriptionDao;
    private final TariffService tariffService;

    public SubscriptionServiceImpl(SubscriptionDao subscriptionDao, TariffService tariffService) {
        this.subscriptionDao = subscriptionDao;
        this.tariffService = tariffService;
    }

    @Override
    public Iterable<Subscription> getByCustomer(Customer customer) {
        return StreamSupport
                .stream(subscriptionDao.findByCustomerId(customer.getId()).spliterator(), true)
                .sorted(SORT_BY_SINCE_THEN_BY_UNTIL)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Subscription> getActiveSubscriptions(Customer customer) {
        return subscriptionDao.findByCustomerIdAndActivePeriodContains(customer.getId(), Instant.now());
    }

    @Override
    public void subscribe(Customer customer, long tariffId) {
        setOneTariffForCustomer(customer, tariffId);    //TODO: add multiply active subscriptions feature
    }

    @Override
    public void setOneTariffForCustomer(Customer customer, long tariffId) {
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
            subscription.setTrafficPerPeriod(tariff.getIncludedTraffic());
            subscription.setActiveSince(now);
            subscription.setActiveUntil(null);
            subscriptionDao.save(subscription);
        }
    }

    @Override
    public void cancelSubscription(Customer customer, long subscriptionId) {
        Subscription subscription = subscriptionDao.findById(subscriptionId).orElseThrow();
        if (customer.getId() != subscription.getCustomer().getId()) {
            throw new ServiceException("The subscription does not belong to the customer");
        }
        subscription.setActiveUntil(Instant.now());
        subscriptionDao.update(subscription);
    }
}
