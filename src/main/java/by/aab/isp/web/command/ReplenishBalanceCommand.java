package by.aab.isp.web.command;

import by.aab.isp.entity.Customer;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class ReplenishBalanceCommand implements Command {
    private final UserService userService;

    public ReplenishBalanceCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        Customer customer = (Customer) req.getAttribute("activeCustomer");
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        userService.replenishBalance(customer, amount);
        return SCHEMA_REDIRECT + "?action=my_account";  //TODO: add pagination for subscriptions
    }
}
