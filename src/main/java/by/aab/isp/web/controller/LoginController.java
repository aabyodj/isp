package by.aab.isp.web.controller;

import static by.aab.isp.Const.DEFAULT_ADMIN_EMAIL;
import static by.aab.isp.Const.DEFAULT_ADMIN_PASSWORD;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.dto.user.CredentialsDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginForm(HttpServletRequest req) {
        req.setAttribute("defaultAdminEmail", DEFAULT_ADMIN_EMAIL);
        req.setAttribute("defaultAdminPassword", DEFAULT_ADMIN_PASSWORD);
        if (req.getAttribute("redirect") == null) {
            String redirect = req.getParameter("redirect");
            if (null == redirect) {
                redirect = "";
            }
            req.setAttribute("redirect", redirect);
        }
        return "login-form";
    }

    @PostMapping("/login")
    public String checkLogin(@RequestParam String email, @RequestParam String password, @RequestParam String redirect, HttpSession session) {
        CredentialsDto credentials = new CredentialsDto();
        credentials.setEmail(email);
        credentials.setPassword(password);
        UserDto user = userService.login(credentials);
        session.setAttribute("userId", user.getId());
        if (redirect.isBlank()) {
            redirect = "/";
        }
        return SCHEMA_REDIRECT + redirect;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return SCHEMA_REDIRECT + "/";
    }
}
