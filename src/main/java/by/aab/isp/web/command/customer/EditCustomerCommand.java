package by.aab.isp.web.command.customer;

import by.aab.isp.entity.*;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.ZoneId;
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
        long customerId = 0;
        try {
            customerId = Long.parseLong(req.getParameter("id"));
        } catch (Exception ignore) {
        }
        Customer customer = userService.getCustomerById(customerId);
        req.setAttribute("customer", customer);
        if (customer.getPayoffDate() != null) {
            req.setAttribute("payoffDate", LocalDate.ofInstant(customer.getPayoffDate(), ZoneId.systemDefault()));
        }
        Tariff tariff = StreamSupport
                .stream(subscriptionService.getActiveSubscriptions(customer).spliterator(), true)
                .map(Subscription::getTariff)
                .findAny()
                .orElse(null);
        req.setAttribute("activeTariff", tariff);
        req.setAttribute("tariffs", tariffService.getAll());
        req.setAttribute("redirect", "?action=manage_customers");   //TODO: determine a referer
        return "jsp/edit-customer.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
