package by.aab.isp.web.command;

import by.aab.isp.entity.Customer;
import by.aab.isp.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SubscribeCommand implements Command {
    private final SubscriptionService subscriptionService;

    public SubscribeCommand(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        Customer customer = (Customer) req.getAttribute("activeCustomer");
        if (null == customer) throw new RuntimeException("Not implemented");    //TODO: implement this
        long tariffId = Long.parseLong(req.getParameter("new-tariff"));
        subscriptionService.subscribe(customer, tariffId);
        return SCHEMA_REDIRECT + "?action=my_account";  //TODO: add pagination - return to the last page
    }
}
