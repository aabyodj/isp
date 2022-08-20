package by.aab.isp.service;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;

public interface SubscriptionService {

    Iterable<Subscription> getByCustomer(Customer customer);

    Iterable<Subscription> getActiveSubscriptions(Customer customer);

    void subscribe(Customer customer, long tariffId);

    void setOneTariffForCustomer(Customer customer, long tariffId);

    void cancelSubscription(Customer customer, long subscriptionId);
}
