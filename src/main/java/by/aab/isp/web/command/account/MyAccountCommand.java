package by.aab.isp.web.command.account;

import by.aab.isp.dto.subscription.SubscriptionDto;
import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.command.Command;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MyAccountCommand extends Command {
    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;

    @Override
    public String execute(HttpServletRequest req) {
        CustomerDto customer = (CustomerDto) req.getAttribute("activeCustomer");
        if (customer != null) {
            List<SubscriptionDto> subscriptions = subscriptionService.getByCustomerId(customer.getId());
            if (!subscriptions.isEmpty()) {
                req.setAttribute("subscriptions", subscriptions);
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
