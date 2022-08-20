package by.aab.isp.web.command.account;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;
import by.aab.isp.entity.Tariff;
import by.aab.isp.entity.User;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.Util;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MyAccountCommand extends Command {
    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;

    public MyAccountCommand(SubscriptionService subscriptionService, TariffService tariffService) {
        this.subscriptionService = subscriptionService;
        this.tariffService = tariffService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Customer customer = (Customer) req.getAttribute("activeCustomer");
        if (customer != null) {
            Iterable<Subscription> subscriptions = subscriptionService.getByCustomer(customer);
            LocalDateTime now = LocalDateTime.now();
            if (subscriptions.spliterator().estimateSize() > 0) {
                req.setAttribute("subscriptions", subscriptions);
                req.setAttribute("now", now);
                req.setAttribute("util", Util.getInstance());
            }
            Set<Tariff> activeTariffs = StreamSupport
                    .stream(subscriptions.spliterator(), true)
                    .filter(subscription ->
                            subscription.getActiveUntil() == null || subscription.getActiveUntil().isAfter(now))
                    .map(Subscription::getTariff)
                    .collect(Collectors.toUnmodifiableSet());
            List<Tariff> tariffs = StreamSupport
                    .stream(tariffService.getAll().spliterator(), true)
                    .filter(tariff -> !activeTariffs.contains(tariff))
                    .collect(Collectors.toList());
            if (!tariffs.isEmpty()) {
                req.setAttribute("tariffs", tariffs);
            }
        }
        req.setAttribute("redirect", "?" + req.getQueryString());
        return "jsp/my-account.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user != null;
    }
}
