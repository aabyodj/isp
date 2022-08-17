package by.aab.isp.web.filter;

import by.aab.isp.entity.User;
import by.aab.isp.web.command.Command;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthorizationFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Command command = (Command) req.getAttribute("command");
        if (command.isAllowedForAnonymous()) {
            super.doFilter(req, res, chain);
            return;
        }
        User user = (User) req.getAttribute("activeUser");
        if (null == user) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (command.isAllowedForUser(user)) {
            super.doFilter(req, res, chain);
            return;
        }
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

}
