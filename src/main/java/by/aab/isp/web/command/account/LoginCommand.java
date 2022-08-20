package by.aab.isp.web.command.account;

import by.aab.isp.entity.User;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class LoginCommand extends Command {
    @Override
    public String execute(HttpServletRequest req) {
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
    public boolean isAllowedForUser(User user) {
        return null == user;
    }
}
