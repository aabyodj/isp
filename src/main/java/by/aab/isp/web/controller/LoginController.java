package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
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
    public String getLoginForm(@RequestParam(defaultValue = "/") String redirect, Model model) {
        model.addAttribute("credentials", new LoginCredentialsDto());
        model.addAttribute("redirect", redirect);
        return "login-form";
    }

    @PostMapping("/login")
    public String checkLogin(@ModelAttribute("credentials") LoginCredentialsDto credentials, Errors errors,
            @ModelAttribute("redirect") String redirect, Model model, HttpSession session) {
        try {
            long userId = userService.login(credentials);
            session.setAttribute("userId", userId);
            return SCHEMA_REDIRECT + redirect;
        } catch (UnauthorizedException e) {
            errors.reject("msg.user.login.error");
            credentials.setPassword(null);
            return "login-form";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return SCHEMA_REDIRECT + "/";
    }
}
