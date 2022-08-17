package by.aab.isp.web.command.customer;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class ReplenishBalanceCommand extends Command {
    private final UserService userService;

    public ReplenishBalanceCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Customer customer = (Customer) req.getAttribute("activeCustomer");
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        userService.replenishBalance(customer, amount);
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Customer;
    }
}
