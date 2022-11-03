package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.dto.subscription.SubscriptionViewDto;
import by.aab.isp.dto.tariff.TariffViewDto;
import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.UpdateCredentialsDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/my_account")
@RequiredArgsConstructor
public class MyAccountController {

    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;
    private final UserService userService;

    @GetMapping
    public String showMyAccount(@RequestAttribute UserDto activeUser, @RequestAttribute(required = false) CustomerDto activeCustomer, Model model) {
        if (activeCustomer != null) {
            List<SubscriptionViewDto> subscriptions = subscriptionService.getByCustomerId(activeCustomer.getId());
            if (!subscriptions.isEmpty()) {
                model.addAttribute("subscriptions", subscriptions);
            }
            List<TariffViewDto> absentTariffs = tariffService.getInactiveForCustomer(activeCustomer.getId());
            if (!absentTariffs.isEmpty()) {
                model.addAttribute("tariffs", absentTariffs);
            }
        }
        return "my-account";
    }

    @PostMapping("/replenish_balance")
    public String replenishBalance(@RequestAttribute CustomerDto activeCustomer, @RequestParam BigDecimal amount, @RequestParam String redirect) {
        userService.replenishBalance(activeCustomer.getId(), amount);
        return SCHEMA_REDIRECT + redirect;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestAttribute CustomerDto activeCustomer, @RequestParam("tariff_id") long tariffId, String redirect) {
        subscriptionService.subscribe(activeCustomer.getId(), tariffId);
        return SCHEMA_REDIRECT + redirect;
    }

    @GetMapping("/cancel_subscription")
    public String cancelSubscription(@RequestAttribute CustomerDto activeCustomer, @RequestParam("subscription_id") long subscriptionId, String redirect) {
        subscriptionService.cancelSubscription(activeCustomer.getId(), subscriptionId);
        if (null == redirect) {
            redirect = "/my_account";
        }
        return SCHEMA_REDIRECT + redirect;
    }

    @PostMapping("/update_credentials")
    public String updateCredentials(@RequestAttribute UserDto activeUser, @RequestParam String email, 
            @RequestParam("new-password1") String newPassword, @RequestParam("new-password2") String repeatPassword, 
            @RequestParam("current-password") String currentPassword, @RequestParam String redirect, HttpSession session) {
        UpdateCredentialsDto dto = new UpdateCredentialsDto();
        dto.setUserId(activeUser.getId());
        dto.setEmail(email);
        if (!Objects.equals(newPassword, repeatPassword)) {
            throw new RuntimeException("Passwords do not match. Handler unimplemented");  //TODO: implement this
        }
        if (null != newPassword && newPassword.isBlank()) {
            newPassword = null;
        }
        dto.setNewPassword(newPassword);
        dto.setCurrentPassword(currentPassword);
        userService.updateCredentials(dto);
        session.invalidate();
        return SCHEMA_REDIRECT + redirect;
    }

    @ExceptionHandler
    public String handleAnonymous(ServletRequestBindingException e, @RequestAttribute(required = false) UserDto activeUser, HttpServletRequest req)
            throws ServletRequestBindingException, UnsupportedEncodingException {
        if (activeUser != null) {
            throw e;
        }
        String redirect = URLEncoder.encode(req.getRequestURL().toString(), "UTF-8");
        return SCHEMA_REDIRECT + "/login?redirect=" + redirect;
    }
}
