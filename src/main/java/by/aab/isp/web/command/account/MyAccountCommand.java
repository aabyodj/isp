package by.aab.isp.web.command.account;

import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.entity.Subscription;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.FormatUtil;
import by.aab.isp.web.command.Command;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MyAccountCommand extends Command {
    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;
    private final FormatUtil util;

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
            List<ShowTariffDto> absentTariffs = tariffService.getInactiveForCustomer(customer.getId());
            if (!absentTariffs.isEmpty()) {
                req.setAttribute("tariffs", absentTariffs);
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
