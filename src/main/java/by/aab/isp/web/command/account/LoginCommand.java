package by.aab.isp.web.command.account;

import static by.aab.isp.Const.DEFAULT_ADMIN_EMAIL;
import static by.aab.isp.Const.DEFAULT_ADMIN_PASSWORD;

import by.aab.isp.dto.user.UserDto;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class LoginCommand extends Command {
    @Override
    public String execute(HttpServletRequest req) {
        req.setAttribute("defaultAdminEmail", DEFAULT_ADMIN_EMAIL);
        req.setAttribute("defaultAdminPassword", DEFAULT_ADMIN_PASSWORD);
        if (req.getAttribute("redirect") == null) {
            String redirect = req.getParameter("redirect");
            if (null == redirect) {
                redirect = "";
            }
            req.setAttribute("redirect", redirect);
        }
        return "jsp/login-form.jsp";
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return null == user;
    }
}
