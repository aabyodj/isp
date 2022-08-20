package by.aab.isp.web.command.customer;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SaveCustomerCommand extends Command {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public SaveCustomerCommand(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        long id = Long.parseLong(req.getParameter("id"));
        String password = req.getParameter("password1");
        if (!Objects.equals(password, req.getParameter("password2"))) {
            throw new RuntimeException("Passwords do not match. Handler unimplemented"); //TODO: implement this
        }
        if (null != password && password.isBlank()) {
            password = null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        customer.setEmail(req.getParameter("email"));
        customer.setActive(req.getParameter("active") != null);
        customer.setBalance(new BigDecimal(req.getParameter("balance")));
        customer.setPermittedOverdraft(new BigDecimal(req.getParameter("permitted-overdraft")));
        String payoffDate = req.getParameter("payoff-date");
        if (payoffDate != null && !payoffDate.isBlank()) {
            customer.setPayoffDate(LocalDate.parse(payoffDate).plusDays(1).atStartOfDay().minusNanos(1000));
        }
        userService.save(customer, password);   //TODO: terminate their session
        long tariffId = Long.parseLong(req.getParameter("tariff"));
        subscriptionService.setOneTariffForCustomer(customer, tariffId);
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
