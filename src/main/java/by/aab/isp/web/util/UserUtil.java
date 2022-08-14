package by.aab.isp.web.util;

import by.aab.isp.entity.Customer;
import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UserUtil {

    private final UserService userService;

    public UserUtil(UserService userService) {
        this.userService = userService;
    }

    public void setUserAttribute(HttpServletRequest req) {
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
