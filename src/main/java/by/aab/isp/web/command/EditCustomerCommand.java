package by.aab.isp.web.command;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Subscription;
import by.aab.isp.entity.Tariff;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.StreamSupport;

public class EditCustomerCommand implements Command {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;

    public EditCustomerCommand(UserService userService, SubscriptionService subscriptionService, TariffService tariffService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.tariffService = tariffService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        long customerId = 0;
        try {
            customerId = Long.parseLong(req.getParameter("id"));
        } catch (Exception ignore) {
        }
        Customer customer = customerId != 0 ? (Customer) userService.getById(customerId)
                                            : new Customer();
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
        return "jsp/edit-customer.jsp";
    }
}
