package by.aab.isp.web.command.account;

import by.aab.isp.entity.User;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class LogoutCommand extends Command {
    @Override
    public String apply(HttpServletRequest req) {
        req.getSession().invalidate();
        return SCHEMA_REDIRECT + req.getContextPath();
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user != null;
    }
}
