package by.aab.isp.service;

import java.util.List;

import by.aab.isp.dto.subscription.SubscriptionDto;

public interface SubscriptionService {

    List<SubscriptionDto> getByCustomerId(long customerId);

    List<SubscriptionDto> getActiveSubscriptions(long customerId);

    void subscribe(long customerId, long tariffId);

    void setOneTariffForCustomer(long customerId, Long tariffId);

    void cancelSubscription(long customerId, long subscriptionId);
}
