package by.aab.isp.web.command.subscription;

import by.aab.isp.entity.Customer;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class CancelSubscriptionCommand implements Command {
    private final SubscriptionService subscriptionService;

    public CancelSubscriptionCommand(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        Customer customer = (Customer) req.getAttribute("activeCustomer");
        if (customer == null) throw new RuntimeException("Not implemented");    //TODO: implement this
        long subscriptionId = Long.parseLong(req.getParameter("subscription_id"));
        subscriptionService.cancelSubscription(customer, subscriptionId);
        return SCHEMA_REDIRECT + "?action=my_account";  //TODO: add pagination
    }
}
