package by.aab.isp.web.command.customer;

import by.aab.isp.dto.CustomerDto;
import by.aab.isp.dto.EmployeeDto;
import by.aab.isp.dto.UserDto;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

@Component
public class SaveCustomerCommand extends Command {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public SaveCustomerCommand(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        String idString = req.getParameter("id");
        Long id = idString != null && !idString.isBlank() ? Long.parseLong(idString)
                                                          : null;
        String password = req.getParameter("password1");
        if (!Objects.equals(password, req.getParameter("password2"))) {
            throw new RuntimeException("Passwords do not match. Handler unimplemented"); //TODO: implement this
        }
        if (null != password && password.isBlank()) {
            password = null;
        }
        CustomerDto customer = new CustomerDto();
        customer.setId(id);
        customer.setEmail(req.getParameter("email"));
        customer.setPassword(password);
        customer.setActive(req.getParameter("active") != null);
        customer.setBalance(new BigDecimal(req.getParameter("balance")));
        customer.setPermittedOverdraft(new BigDecimal(req.getParameter("permitted-overdraft")));
        String payoffDate = req.getParameter("payoff-date");
        if (payoffDate != null && !payoffDate.isBlank()) {
            customer.setPayoffDate(LocalDate.parse(payoffDate).plusDays(1).atStartOfDay().minusNanos(1000));
        }
        customer = (CustomerDto) userService.save(customer);   //TODO: terminate their session
        String tariffParam = req.getParameter("tariff");
        Long tariffId = !tariffParam.equals("none") ? Long.parseLong(tariffParam) : null;
        subscriptionService.setOneTariffForCustomer(customer.getId(), tariffId);    //TODO: use extended DTO for this
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof EmployeeDto;
    }
}
