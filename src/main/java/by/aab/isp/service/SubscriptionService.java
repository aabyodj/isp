package by.aab.isp.service;

import java.util.List;

import by.aab.isp.service.dto.subscription.SubscriptionViewDto;

public interface SubscriptionService {

    List<SubscriptionViewDto> getByCustomerId(long customerId);

    List<SubscriptionViewDto> getActiveSubscriptions(long customerId);

    void subscribe(long customerId, long tariffId);

    void setOneTariffForCustomer(long customerId, Long tariffId);

    void cancelSubscription(long customerId, long subscriptionId);
}
