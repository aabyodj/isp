package by.aab.isp.web.command.account;

import by.aab.isp.dto.CredentialsDto;
import by.aab.isp.dto.UserDto;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

@Component
public class CheckLoginCommand extends Command {
    private final UserService userService;

    public CheckLoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        CredentialsDto credentials = new CredentialsDto();
        credentials.setEmail(req.getParameter("email"));
        credentials.setPassword(req.getParameter("password"));
        String redirect = req.getParameter("redirect");
        if ("?action=check_login".equals(redirect)) {   //FIXME: this is a dirty workaround. Must reliably determine referer instead
            redirect = "";
        }
        UserDto user = userService.login(credentials);
        HttpSession session = req.getSession();
        session.setAttribute("userId", user.getId());
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return null == user;
    }
}
