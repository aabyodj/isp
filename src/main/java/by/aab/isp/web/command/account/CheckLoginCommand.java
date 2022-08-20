package by.aab.isp.web.command.account;

import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class CheckLoginCommand extends Command {
    private final UserService userService;

    public CheckLoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String redirect = req.getParameter("redirect");
        if ("?action=check_login".equals(redirect)) {   //FIXME: this is a dirty workaround. Must reliably determine referer instead
            redirect = "";
        }
        User user = userService.login(email, password);
        HttpSession session = req.getSession();
        session.setAttribute("userId", user.getId());
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return null == user;
    }
}
