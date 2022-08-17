package by.aab.isp.web.command.account;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;
import by.aab.isp.entity.Tariff;
import by.aab.isp.entity.User;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.Comparator;
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

    private static final Comparator<Subscription> SORT_BY_SINCE_THEN_BY_UNTIL = (s1, s2) -> {
        if (s1.getActiveSince() != null && s2.getActiveSince() != null) {
            return s1.getActiveSince().compareTo(s2.getActiveSince());
        }
        if (s1.getActiveSince() != null) return 1;
        if (s2.getActiveSince() != null) return -1;
        if (s1.getActiveUntil() != null && s2.getActiveUntil() != null) {
            return s1.getActiveUntil().compareTo(s2.getActiveUntil());
        }
        if (s1.getActiveUntil() != null) return -1;
        if (s2.getActiveUntil() != null) return 1;
        return 0;
    };

    @Override
    public String apply(HttpServletRequest req) {
        Customer customer = (Customer) req.getAttribute("activeCustomer");
        if (customer != null) {
            List<Subscription> subscriptions = StreamSupport
                    .stream(subscriptionService.getAll(customer).spliterator(), false)
                    .sorted(SORT_BY_SINCE_THEN_BY_UNTIL)
                    .collect(Collectors.toList());
            Instant now = Instant.now();
            if (!subscriptions.isEmpty()) {
                req.setAttribute("subscriptions", subscriptions);
                req.setAttribute("now", now);
            }
            Set<Tariff> activeTariffs = subscriptions.stream()
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
        req.setAttribute("redirect", req.getQueryString());
        return "jsp/my-account.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user != null;
    }
}
