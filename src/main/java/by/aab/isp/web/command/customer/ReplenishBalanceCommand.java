package by.aab.isp.web.command.customer;

import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

@Component
public class ReplenishBalanceCommand extends Command {
    private final UserService userService;

    public ReplenishBalanceCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        CustomerDto customer = (CustomerDto) req.getAttribute("activeCustomer");
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));
        userService.replenishBalance(customer.getId(), amount);
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof CustomerDto;
    }
}
