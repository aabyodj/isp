package by.aab.isp.web.command.subscription;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.User;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class CancelSubscriptionCommand extends Command {
    private final SubscriptionService subscriptionService;

    public CancelSubscriptionCommand(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Customer customer = (Customer) req.getAttribute("activeCustomer");
        if (customer == null) throw new RuntimeException("Not implemented");    //TODO: implement this
        long subscriptionId = Long.parseLong(req.getParameter("subscription_id"));
        subscriptionService.cancelSubscription(customer, subscriptionId);
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Customer;
    }
}
