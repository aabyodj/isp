package by.aab.isp.web.command;

import by.aab.isp.entity.User;
import javax.servlet.http.HttpServletRequest;

public abstract class Command {
    public abstract String execute(HttpServletRequest req);

    public abstract boolean isAllowedForUser(User user);

    public boolean isAllowedForAnonymous() {
        return isAllowedForUser(null);
    }
}
