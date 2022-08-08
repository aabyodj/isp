package by.aab.isp.web.command;

import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class CheckLoginCommand implements Command {
    private final UserService userService;

    public CheckLoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = userService.login(email, password);
        HttpSession session = req.getSession();
        session.setAttribute("userId", user.getId());
        return SCHEMA_REDIRECT + req.getContextPath();
    }
}
