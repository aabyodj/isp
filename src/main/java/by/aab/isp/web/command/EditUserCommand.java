package by.aab.isp.web.command;

import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

public class EditUserCommand implements Command {

    private final UserService userService;

    public EditUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        long userId = 0;
        try {
            userId = Long.parseLong(req.getParameter("user_id"));
        } catch (Exception ignore) {
        }
        User user = userId != 0 ? userService.getById(userId)
                                : new User();
        req.setAttribute("user", user);
        req.setAttribute("userRoles", User.Role.values());
        return "jsp/edit-user.jsp";
    }
}
