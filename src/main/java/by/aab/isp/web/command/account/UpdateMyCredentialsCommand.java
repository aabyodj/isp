package by.aab.isp.web.command.account;

import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class UpdateMyCredentialsCommand extends Command {
    private final UserService userService;

    public UpdateMyCredentialsCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        User user = (User) req.getAttribute("activeUser");
        String newEmail = req.getParameter("email");
        String newPassword = req.getParameter("new-password1");
        if (!Objects.equals(newPassword, req.getParameter("new-password2"))) {
            throw new RuntimeException("Passwords do not match. Handler unimplemented");  //TODO: implement this
        }
        if (null != newPassword && newPassword.isBlank()) {
            newPassword = null;
        }
        String currentPassword = req.getParameter("current-password");
        userService.updateCredentials(user, newEmail, newPassword, currentPassword);
        String redirect = req.getParameter("redirect");
        req.getSession().invalidate();
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user != null;
    }
}
