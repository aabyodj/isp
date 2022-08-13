package by.aab.isp.web.command;

import by.aab.isp.entity.Customer;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SaveCustomerCommand implements Command {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public SaveCustomerCommand(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        Customer customer = new Customer();
        customer.setId(Long.parseLong(req.getParameter("id")));
        customer.setEmail(req.getParameter("email"));
        userService.save(customer);
        long tariffId = Long.parseLong(req.getParameter("tariff"));
        subscriptionService.setOneTariffForCustomer(tariffId, customer);
        return SCHEMA_REDIRECT + "?action=manage_customers";
    }
}
