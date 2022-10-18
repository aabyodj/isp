package by.aab.isp.web.command.customer;

import by.aab.isp.dto.subscription.SubscriptionDto;
import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
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
        CustomerDto customer = userService.getCustomerById(id != null ? Long.parseLong(id)
                                                                      : null);
        req.setAttribute("customer", customer);
        if (customer.getId() != null) {
            ShowTariffDto tariff = subscriptionService.getActiveSubscriptions(customer.getId())
                    .stream()
                    .map(SubscriptionDto::getTariff)
                    .findAny()
                    .orElse(null);
            req.setAttribute("activeTariff", tariff);
        }
        req.setAttribute("tariffs", tariffService.getAll());
        req.setAttribute("redirect", "?action=manage_customers");   //TODO: determine a referer
        return "jsp/edit-customer.jsp";
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof EmployeeDto;
    }
}
