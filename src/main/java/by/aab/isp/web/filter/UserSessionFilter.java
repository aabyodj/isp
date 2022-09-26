package by.aab.isp.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.CommandFactory;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserSessionFilter extends HttpFilter {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            log.trace("Initializing...");
            ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
            CommandFactory.getInstance().init(context);
            userService = context.getBean(UserService.class);
            log.info("Initialization complete");
        } catch (Throwable e) {
            log.fatal("Failed to initialize", e);
            throw e;
        }
    }

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        setUserAttribute(req);
        chain.doFilter(req, resp);
    }

    private void setUserAttribute(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (null == session) {
            log.trace("Anonymous user. No session found");
            return;
        }
        Long userId = (Long) session.getAttribute("userId");
        if (null == userId) {
            log.trace("Anonymous user. Session id=" + session.getId());
            return;
        }
        User user = userService.getById(userId);
        req.setAttribute("activeUser", user);
        log.trace(user + ". Session id=" + session.getId());
        if (user instanceof Customer) {
            req.setAttribute("activeCustomer", user);
        } else if (user instanceof Employee) {
            req.setAttribute("activeEmployee", user);
        }
    }
}
