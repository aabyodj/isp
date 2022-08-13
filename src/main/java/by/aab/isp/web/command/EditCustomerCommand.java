package by.aab.isp.web.command;

import by.aab.isp.entity.Customer;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

public class EditCustomerCommand implements Command {

    private final UserService userService;
    private final TariffService tariffService;

    public EditCustomerCommand(UserService userService, TariffService tariffService) {
        this.userService = userService;
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
        req.setAttribute("tariffs", tariffService.getAll());
        return "jsp/edit-customer.jsp";
    }
}
