package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.dto.subscription.SubscriptionViewDto;
import by.aab.isp.dto.tariff.TariffViewDto;
import by.aab.isp.dto.user.CustomerViewDto;
import by.aab.isp.dto.user.UpdateCredentialsDto;
import by.aab.isp.dto.user.UserViewDto;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UnauthorizedException;
import by.aab.isp.service.UserService;
import by.aab.isp.validator.UpdateCredentialsDtoValidator;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/my_account")
@RequiredArgsConstructor
public class MyAccountController {

    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;
    private final UserService userService;
    private final UpdateCredentialsDtoValidator credentialsValidator;

    @GetMapping
    public String showMyAccount(@RequestAttribute UserViewDto activeUser, @RequestAttribute(required = false) CustomerViewDto activeCustomer, Model model) {
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
    public String replenishBalance(@RequestAttribute CustomerViewDto activeCustomer, @RequestParam BigDecimal amount, @RequestParam String redirect) {
        userService.replenishBalance(activeCustomer.getId(), amount);
        return SCHEMA_REDIRECT + redirect;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestAttribute CustomerViewDto activeCustomer, @RequestParam("tariff_id") long tariffId, String redirect) {
        subscriptionService.subscribe(activeCustomer.getId(), tariffId);
        return SCHEMA_REDIRECT + redirect;
    }

    @GetMapping("/cancel_subscription")
    public String cancelSubscription(@RequestAttribute CustomerViewDto activeCustomer, @RequestParam("subscription_id") long subscriptionId, String redirect) {
        subscriptionService.cancelSubscription(activeCustomer.getId(), subscriptionId);
        if (null == redirect) {
            redirect = "/my_account";
        }
        return SCHEMA_REDIRECT + redirect;
    }

    @ModelAttribute("credentials")
    public UpdateCredentialsDto initUpdateCredentials(@RequestAttribute UserViewDto activeUser) {
        UpdateCredentialsDto credentials =  new UpdateCredentialsDto();
        credentials.setUserId(activeUser.getId());
        credentials.setEmail(activeUser.getEmail());
        return credentials;
    }

    @PostMapping("/update_credentials")
    public String updateCredentials(@RequestAttribute UserViewDto activeUser,
            @Valid @ModelAttribute("credentials") UpdateCredentialsDto credentials, BindingResult bindingResult,
            @ModelAttribute("redirect") String redirect, HttpSession session) {
        if (!bindingResult.hasErrors()) {
            try {
                userService.updateCredentials(credentials);
                session.invalidate();
                return SCHEMA_REDIRECT + redirect;
            } catch (UnauthorizedException e) {
                bindingResult.rejectValue("currentPassword", "msg.account.wrong-password");
            }
        }
        credentials.setCurrentPassword(null);
        return "my-account";
    }

    @InitBinder("credentials")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(credentialsValidator);
    }

    @ExceptionHandler
    public String handleAnonymous(ServletRequestBindingException e, @RequestAttribute(required = false) UserViewDto activeUser, HttpServletRequest req)
            throws ServletRequestBindingException, UnsupportedEncodingException {
        if (activeUser != null) {
            throw e;
        }
        String redirect = URLEncoder.encode(req.getRequestURL().toString(), "UTF-8");
        return SCHEMA_REDIRECT + "/login?redirect=" + redirect;
    }
}
