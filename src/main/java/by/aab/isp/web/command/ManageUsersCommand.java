package by.aab.isp.web.command;

import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

public class ManageUsersCommand implements Command {

    private final UserService userService;

    public ManageUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        req.setAttribute("users", userService.getAll());
        return "jsp/manage-users.jsp";
    }
}
