package by.aab.isp.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import by.aab.isp.service.UserService;
import by.aab.isp.service.dto.user.CustomerViewDto;
import by.aab.isp.service.dto.user.EmployeeViewDto;
import by.aab.isp.service.dto.user.UserViewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class UserSessionInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        setUserAttribute(request);
        return true;
    }

    private void setUserAttribute(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (null == session) {
            log.debug("Anonymous user. No session found");
            return;
        }
        Long userId = (Long) session.getAttribute("userId");
        if (null == userId) {
            log.debug("Anonymous user. Session id=" + session.getId());
            return;
        }
        UserViewDto user = userService.getById(userId);
        req.setAttribute("activeUser", user);
        log.debug(user + ". Session id=" + session.getId());
        if (user instanceof CustomerViewDto) {
            req.setAttribute("activeCustomer", user);
        } else if (user instanceof EmployeeViewDto) {
            req.setAttribute("activeEmployee", user);
        }
    }
}
