package by.aab.isp.service;

import by.aab.isp.entity.Subscription;

public interface SubscriptionService {

    Iterable<Subscription> getByCustomerId(long customerId);

    Iterable<Subscription> getActiveSubscriptions(long customerId);

    void subscribe(long customerId, long tariffId);

    void setOneTariffForCustomer(long customerId, Long tariffId);

    void cancelSubscription(long customerId, long subscriptionId);
}
