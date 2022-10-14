package by.aab.isp.web.command.account;

import by.aab.isp.dto.UserDto;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

@Component
public class LogoutCommand extends Command {
    @Override
    public String execute(HttpServletRequest req) {
        req.getSession().invalidate();
        return SCHEMA_REDIRECT + req.getContextPath();
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user != null;
    }
}
