package by.aab.isp.web.command.account;

import by.aab.isp.dto.CustomerDto;
import by.aab.isp.dto.UserDto;
import by.aab.isp.entity.Subscription;
import by.aab.isp.entity.Tariff;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.FormatUtil;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class MyAccountCommand extends Command {
    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;
    private final FormatUtil util;

    public MyAccountCommand(SubscriptionService subscriptionService, TariffService tariffService, FormatUtil util) {
        this.subscriptionService = subscriptionService;
        this.tariffService = tariffService;
		this.util = util;
    }

    @Override
    public String execute(HttpServletRequest req) {
        CustomerDto customer = (CustomerDto) req.getAttribute("activeCustomer");
        if (customer != null) {
            Iterable<Subscription> subscriptions = subscriptionService.getByCustomerId(customer.getId());
            LocalDateTime now = LocalDateTime.now();
            if (subscriptions.spliterator().estimateSize() > 0) {
                req.setAttribute("subscriptions", subscriptions);
                req.setAttribute("now", now);
                req.setAttribute("util", util);
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
    public boolean isAllowedForUser(UserDto user) {
        return user != null;
    }
}
