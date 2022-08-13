package by.aab.isp.web.command;

import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

public class ManageCustomersCommand implements Command {

    private final UserService userService;

    public ManageCustomersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        req.setAttribute("customers", userService.getAllCustomers());
        return "jsp/manage-customers.jsp";
    }
}
