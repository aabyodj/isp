package by.aab.isp.web.command.subscription;

import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

@Component
public class SubscribeCommand extends Command {
    private final SubscriptionService subscriptionService;

    public SubscribeCommand(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        CustomerDto customer = (CustomerDto) req.getAttribute("activeCustomer");
        if (null == customer) {
            throw new RuntimeException("Not implemented");    //TODO: implement this
        }
        long tariffId = Long.parseLong(req.getParameter("tariff_id"));
        subscriptionService.subscribe(customer.getId(), tariffId);
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof CustomerDto;
    }
}
