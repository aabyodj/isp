package by.aab.isp.web.command;

import by.aab.isp.dto.UserDto;

import javax.servlet.http.HttpServletRequest;

public abstract class Command {
    public abstract String execute(HttpServletRequest req);

    public abstract boolean isAllowedForUser(UserDto user);

    public boolean isAllowedForAnonymous() {
        return isAllowedForUser(null);
    }
}
