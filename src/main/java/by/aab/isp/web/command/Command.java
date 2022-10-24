package by.aab.isp.web.command;

import javax.servlet.http.HttpServletRequest;

import by.aab.isp.dto.user.UserDto;

public abstract class Command {
    public abstract String execute(HttpServletRequest req);

    public abstract boolean isAllowedForUser(UserDto user);

    public boolean isAllowedForAnonymous() {
        return isAllowedForUser(null);
    }
}
