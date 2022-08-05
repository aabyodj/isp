package by.aab.isp.web.command;

import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SaveUserCommand implements Command {

    private final UserService userService;

    public SaveUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        User user = new User(
                Long.parseLong(req.getParameter("id")),
                req.getParameter("email"),
                User.Role.valueOf(req.getParameter("role"))
        );
        userService.save(user);
        return SCHEMA_REDIRECT + "?action=manage_users";
    }
}
