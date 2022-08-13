package by.aab.isp.web.command;

import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

public class ManageEmployeesCommand implements Command {
    private final UserService userService;

    public ManageEmployeesCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        req.setAttribute("employees", userService.getAllEmployees());
        return "jsp/manage-employees.jsp";
    }
}
