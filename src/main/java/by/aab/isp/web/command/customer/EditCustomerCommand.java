package by.aab.isp.web.command.customer;

import by.aab.isp.entity.*;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import java.util.stream.StreamSupport;

public class EditCustomerCommand extends Command {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;

    public EditCustomerCommand(UserService userService, SubscriptionService subscriptionService, TariffService tariffService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.tariffService = tariffService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        String id = req.getParameter("id");
        Customer customer = userService.getCustomerById(id != null ? Long.parseLong(id)
                                                                   : null);
        req.setAttribute("customer", customer);
        if (customer.getId() != null) {
            Tariff tariff = StreamSupport
                    .stream(subscriptionService.getActiveSubscriptions(customer).spliterator(), true)
                    .map(Subscription::getTariff)
                    .findAny()
                    .orElse(null);
            req.setAttribute("activeTariff", tariff);
        }
        req.setAttribute("tariffs", tariffService.getAll());
        req.setAttribute("redirect", "?action=manage_customers");   //TODO: determine a referer
        return "jsp/edit-customer.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
