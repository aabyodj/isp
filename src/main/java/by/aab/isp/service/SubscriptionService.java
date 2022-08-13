package by.aab.isp.service;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;

public interface SubscriptionService {

    Iterable<Subscription> getActiveSubscriptions(Customer customer);

    void setOneTariffForCustomer(long tariffId, Customer customer);
}
