package by.aab.isp.web.command.subscription;

import by.aab.isp.dto.CustomerDto;
import by.aab.isp.dto.UserDto;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

@Component
public class CancelSubscriptionCommand extends Command {
    private final SubscriptionService subscriptionService;

    public CancelSubscriptionCommand(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        CustomerDto customer = (CustomerDto) req.getAttribute("activeCustomer");
        if (customer == null) {
            throw new RuntimeException("Not implemented");    //TODO: implement this
        }
        long subscriptionId = Long.parseLong(req.getParameter("subscription_id"));
        subscriptionService.cancelSubscription(customer.getId(), subscriptionId);
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof CustomerDto;
    }
}
