package by.aab.isp.web.filter;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.ServiceFactory;
import by.aab.isp.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class UserSessionFilter extends HttpFilter {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = ServiceFactory.getInstance().getService(UserService.class);
    }

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        setUserAttribute(req);
        chain.doFilter(req, resp);
    }

    private void setUserAttribute(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (null == session) return;
        Long userId = (Long) session.getAttribute("userId");
        if (null == userId) return;
        User user = userService.getById(userId);
        req.setAttribute("activeUser", user);
        if (user instanceof Customer) {
            req.setAttribute("activeCustomer", user);
        } else if (user instanceof Employee) {
            req.setAttribute("activeEmployee", user);
        }
    }
}
