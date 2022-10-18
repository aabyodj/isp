package by.aab.isp.service.impl;

import static by.aab.isp.Const.LDT_FOR_AGES;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import by.aab.isp.aspect.AutoLogged;
import by.aab.isp.dto.converter.SubscriptionConverter;
import by.aab.isp.dto.subscription.SubscriptionDto;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;
import by.aab.isp.entity.Tariff;
import by.aab.isp.repository.CustomerRepository;
import by.aab.isp.repository.OrderOffsetLimit;
import by.aab.isp.repository.SubscriptionRepository;
import by.aab.isp.repository.TariffRepository;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.SubscriptionService;
import lombok.RequiredArgsConstructor;

@Service("subscriptionService")
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final CustomerRepository customerRepository;;
    private final SubscriptionRepository subscriptionRepository;
    private final TariffRepository tariffRepository;
    private final SubscriptionConverter subscriptionConverter;

    private static final List<OrderOffsetLimit.Order> ORDER_BY_SINCE_THEN_BY_UNTIL = List.of(
            new OrderOffsetLimit.Order("activeSince", true),
            new OrderOffsetLimit.Order("activeUntil", true)
    );

    @AutoLogged
    @Override
    public List<SubscriptionDto> getByCustomerId(long customerId) {
        OrderOffsetLimit orderOffsetLimit = new OrderOffsetLimit();
        orderOffsetLimit.setOrderList(ORDER_BY_SINCE_THEN_BY_UNTIL);
        LocalDateTime now = LocalDateTime.now();
        return subscriptionRepository.findByCustomerId(customerId, orderOffsetLimit)
                .stream()
                .map(subscription -> subscriptionConverter.toDto(subscription, now))
                .collect(Collectors.toList());
    }

    @AutoLogged
    @Override
    public List<SubscriptionDto> getActiveSubscriptions(long customerId) {
        LocalDateTime now = LocalDateTime.now();
        return subscriptionRepository.findByCustomerIdAndActivePeriodContains(customerId, now)
                .stream()
                .map(subscription -> subscriptionConverter.toDto(subscription, now))
                .collect(Collectors.toList());
    }

    @AutoLogged
    @Override
    public void subscribe(long customerId, long tariffId) {
        setOneTariffForCustomer(customerId, tariffId);    //TODO: add multiply active subscriptions feature
    }

    @AutoLogged
    @Override
    @Transactional
    public void setOneTariffForCustomer(long customerId, Long tariffId) {
        LocalDateTime now = LocalDateTime.now();
        Iterable<Subscription> subscriptions = subscriptionRepository.findByCustomerIdAndActivePeriodContains(customerId, now);
        boolean alreadySet = false;
        for (Subscription subscription : subscriptions) {
            if (subscription.getTariff().getId().equals(tariffId)) {
                alreadySet = true;
            } else {
                subscription.setActiveUntil(now);
                subscriptionRepository.update(subscription);
            }
        }
        if (!alreadySet && tariffId != null) {
            Customer customer = customerRepository.findById(customerId).orElseThrow();
            Tariff tariff = tariffRepository.findById(tariffId).orElseThrow();
            Subscription subscription = new Subscription();
            subscription.setCustomer(customer);
            subscription.setTariff(tariff);
            subscription.setPrice(tariff.getPrice());
            subscription.setTrafficPerPeriod(tariff.getIncludedTraffic());
            subscription.setActiveSince(now);
            subscription.setActiveUntil(LDT_FOR_AGES);
            subscriptionRepository.save(subscription);
        }
    }

    @AutoLogged
    @Override
    @Transactional
    public void cancelSubscription(long customerId, long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();
        if (customerId != subscription.getCustomer().getId()) {
            throw new ServiceException("The subscription does not belong to the customer");
        }
        subscription.setActiveUntil(LocalDateTime.now());
        subscriptionRepository.update(subscription);
    }
}
