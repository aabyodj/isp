package by.aab.isp.web.command;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Tariff;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SaveCustomerCommand implements Command {

    private final UserService userService;
    private final TariffService tariffService;

    public SaveCustomerCommand(UserService userService, TariffService tariffService) {
        this.userService = userService;
        this.tariffService = tariffService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        Customer customer = new Customer();
        customer.setId(Long.parseLong(req.getParameter("id")));
        customer.setEmail(req.getParameter("email"));
        long tariffId = Long.parseLong(req.getParameter("tariff"));
        if (tariffId != 0) {
            Tariff tariff = tariffService.getById(tariffId);
            customer.setTariff(tariff);
        }
        userService.save(customer);
        return SCHEMA_REDIRECT + "?action=manage_customers";
    }
}
