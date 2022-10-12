package by.aab.isp.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import by.aab.isp.dao.OrderOffsetLimit;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;
import by.aab.isp.entity.Tariff;
import by.aab.isp.repository.SubscriptionRepository;
import by.aab.isp.repository.TariffRepository;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.SubscriptionService;
import lombok.RequiredArgsConstructor;

@Service("subscriptionService")
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final TariffRepository tariffRepository;

    private static final List<OrderOffsetLimit.Order> ORDER_BY_SINCE_THEN_BY_UNTIL = List.of(
            new OrderOffsetLimit.Order("activeSince", true),
            new OrderOffsetLimit.Order("activeUntil", true)
    );

    @Override
    public Iterable<Subscription> getByCustomer(Customer customer) {
        OrderOffsetLimit orderOffsetLimit = new OrderOffsetLimit();
        orderOffsetLimit.setOrderList(ORDER_BY_SINCE_THEN_BY_UNTIL);
        return subscriptionRepository.findByCustomerId(customer.getId(), orderOffsetLimit);
    }

    @Override
    public Iterable<Subscription> getActiveSubscriptions(Customer customer) {
        return subscriptionRepository.findByCustomerIdAndActivePeriodContains(customer.getId(), LocalDateTime.now());
    }

    @Override
    public void subscribe(Customer customer, long tariffId) {
        setOneTariffForCustomer(customer, tariffId);    //TODO: add multiply active subscriptions feature
    }

    @Override
    @Transactional
    public void setOneTariffForCustomer(Customer customer, Long tariffId) {
        Iterable<Subscription> subscriptions = getActiveSubscriptions(customer);
        boolean alreadySet = false;
        LocalDateTime now = LocalDateTime.now();
        for (Subscription subscription : subscriptions) {
            if (subscription.getTariff().getId().equals(tariffId)) {
                alreadySet = true;
            } else {
                subscription.setActiveUntil(now);
                subscriptionRepository.update(subscription);
            }
        }
        if (!alreadySet && tariffId != null) {
            Tariff tariff = tariffRepository.findById(tariffId).orElseThrow();
            Subscription subscription = new Subscription();
            subscription.setCustomer(customer);
            subscription.setTariff(tariff);
            subscription.setPrice(tariff.getPrice());
            subscription.setTrafficPerPeriod(tariff.getIncludedTraffic());
            subscription.setActiveSince(now);
            subscription.setActiveUntil(null);
            subscriptionRepository.save(subscription);
        }
    }

    @Override
    @Transactional
    public void cancelSubscription(Customer customer, long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();
        if ((long) customer.getId() != subscription.getCustomer().getId()) {
            throw new ServiceException("The subscription does not belong to the customer");
        }
        subscription.setActiveUntil(LocalDateTime.now());
        subscriptionRepository.update(subscription);
    }
}
