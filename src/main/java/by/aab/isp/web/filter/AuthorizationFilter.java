package by.aab.isp.web.filter;

import by.aab.isp.entity.User;
import by.aab.isp.web.command.Command;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class AuthorizationFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Command command = (Command) req.getAttribute("command");
        if (command.isAllowedForAnonymous()) {
            log.trace("Not requiring authentication for command '" + command.getClass() + "'");
            super.doFilter(req, res, chain);
            return;
        }
        User user = (User) req.getAttribute("activeUser");
        if (null == user) {
            log.trace("Rejected anonymous user for command '" + command.getClass() + "'");
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (command.isAllowedForUser(user)) {
            log.trace("Accepted user " + user + " for command '" + command.getClass() + "'");
            super.doFilter(req, res, chain);
            return;
        }
        log.warn("Rejected user " + user + " for command '" + command.getClass() + "'");
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

}
