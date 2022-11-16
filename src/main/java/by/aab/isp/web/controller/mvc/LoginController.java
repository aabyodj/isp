package by.aab.isp.web.controller.mvc;

import javax.annotation.security.PermitAll;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    @PermitAll
    public String getLoginForm(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) String updated, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        model.addAttribute("updated", updated);
        return "login-form";
    }
}
