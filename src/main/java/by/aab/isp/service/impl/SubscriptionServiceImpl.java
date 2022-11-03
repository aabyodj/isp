package by.aab.isp.service.impl;

import static by.aab.isp.Const.LDT_FOR_AGES;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import by.aab.isp.aspect.AutoLogged;
import by.aab.isp.dto.subscription.SubscriptionViewDto;
import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;
import by.aab.isp.entity.Tariff;
import by.aab.isp.repository.CustomerRepository;
import by.aab.isp.repository.SubscriptionRepository;
import by.aab.isp.repository.TariffRepository;
import by.aab.isp.service.Now;
import by.aab.isp.service.ServiceException;
import by.aab.isp.service.SubscriptionService;
import lombok.RequiredArgsConstructor;

@Service("subscriptionService")
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final CustomerRepository customerRepository;;
    private final SubscriptionRepository subscriptionRepository;
    private final TariffRepository tariffRepository;
    private final ConversionService conversionService;
//    private final SubscriptionToSubscriptionDtoConverter subscriptionConverter;

    @Autowired
    private Now now;

    private static final Sort ORDER_BY_SINCE_THEN_BY_UNTIL = Sort.by("activeSince", "activeUntil");

    @AutoLogged
    @Override
    public List<SubscriptionViewDto> getByCustomerId(long customerId) {
        return StreamSupport.stream(subscriptionRepository.findByCustomerId(customerId, ORDER_BY_SINCE_THEN_BY_UNTIL).spliterator(), false)
                .map(subscription -> conversionService.convert(subscription, SubscriptionViewDto.class))
                .collect(Collectors.toList());
    }

    @AutoLogged
    @Override
    public List<SubscriptionViewDto> getActiveSubscriptions(long customerId) {
        return subscriptionRepository.findByCustomerIdAndActivePeriodContains(customerId, now.getLocalDateTime())
                .stream()
                .map(subscription -> conversionService.convert(subscription, SubscriptionViewDto.class))
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
        Iterable<Subscription> subscriptions = subscriptionRepository.findByCustomerIdAndActivePeriodContains(customerId, now.getLocalDateTime());
        boolean alreadySet = false;
        for (Subscription subscription : subscriptions) {
            if (subscription.getTariff().getId().equals(tariffId)) {
                alreadySet = true;
            } else {
                subscription.setActiveUntil(now.getLocalDateTime());
                subscriptionRepository.save(subscription);
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
            subscription.setActiveSince(now.getLocalDateTime());
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
        subscription.setActiveUntil(now.getLocalDateTime());
        subscriptionRepository.save(subscription);
    }
}
