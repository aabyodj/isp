package by.aab.isp.web.command.customer;

import by.aab.isp.entity.Customer;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

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
        customer.setBalance(new BigDecimal(req.getParameter("balance")));
        customer.setPermittedOverdraft(new BigDecimal(req.getParameter("permitted-overdraft")));
        String payoffDate = req.getParameter("payoff-date");
        if (payoffDate != null && !payoffDate.isBlank()) {
            customer.setPayoffDate(Instant.from(LocalDate.parse(payoffDate).atStartOfDay(ZoneId.systemDefault())));
        }
        userService.save(customer);
        long tariffId = Long.parseLong(req.getParameter("tariff"));
        subscriptionService.setOneTariffForCustomer(customer, tariffId);
        return SCHEMA_REDIRECT + "?action=manage_customers";
    }
}
