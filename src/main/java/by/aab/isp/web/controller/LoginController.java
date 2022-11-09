package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.dto.user.LoginCredentialsDto;
import by.aab.isp.service.UnauthorizedException;
import by.aab.isp.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginForm(@RequestParam(defaultValue = "/") String redirect,
            @RequestParam(name = "logged-out", required = false) String loggedOut,
            @RequestParam(required = false) String updated, Model model) {
        model.addAttribute("loggedOut", loggedOut);
        model.addAttribute("updated", updated);
        model.addAttribute("credentials", new LoginCredentialsDto());
        model.addAttribute("redirect", redirect);
        return "login-form";
    }

    @PostMapping("/login")
    public String checkLogin(@ModelAttribute("credentials") LoginCredentialsDto credentials, BindingResult bindingResult,
            @ModelAttribute("redirect") String redirect, Model model, HttpServletRequest req, HttpSession session) {
        try {
            long userId = userService.login(credentials);
            req.changeSessionId();
            session.setAttribute("userId", userId);
            return SCHEMA_REDIRECT + redirect;
        } catch (UnauthorizedException e) {
            bindingResult.reject("msg.user.login.error");
            credentials.setPassword(null);
            return "login-form";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userId");
        return SCHEMA_REDIRECT + "/login?logged-out";
    }
}
