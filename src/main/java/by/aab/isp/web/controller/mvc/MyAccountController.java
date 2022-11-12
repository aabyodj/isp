package by.aab.isp.web.controller.mvc;

import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UnauthorizedException;
import by.aab.isp.service.UserService;
import by.aab.isp.service.dto.subscription.SubscriptionViewDto;
import by.aab.isp.service.dto.tariff.TariffViewDto;
import by.aab.isp.service.dto.user.CustomerViewDto;
import by.aab.isp.service.dto.user.UpdateCredentialsDto;
import by.aab.isp.service.dto.user.UserViewDto;
import by.aab.isp.service.validator.UpdateCredentialsDtoValidator;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/my_account")
@PreAuthorize("authenticated")
@RequiredArgsConstructor
public class MyAccountController {

    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;
    private final UserService userService;
    private final UpdateCredentialsDtoValidator credentialsValidator;

    @ModelAttribute("subscriptions")
    List<SubscriptionViewDto> getSubscriptions(@RequestAttribute(required = false) CustomerViewDto activeCustomer) {
        if (null == activeCustomer) {
            return null;
        }
        List<SubscriptionViewDto> subscriptions = subscriptionService.getByCustomerId(activeCustomer.getId());
        return !subscriptions.isEmpty() ? subscriptions : null;
    }

    @ModelAttribute("tariffs")
    List<TariffViewDto> getAvailableTariffs(@RequestAttribute(required = false) CustomerViewDto activeCustomer) {
        if (null == activeCustomer) {
            return null;
        }
        List<TariffViewDto> absentTariffs = tariffService.getInactiveForCustomer(activeCustomer.getId());
        return !absentTariffs.isEmpty() ? absentTariffs : null;
    }

    @GetMapping
    public String showMyAccount() {
        return "my-account";
    }

    @PostMapping("/replenish_balance")
    public String replenishBalance(@RequestAttribute CustomerViewDto activeCustomer,
            @RequestParam BigDecimal amount, @RequestParam(defaultValue = "/my_account") String redirect) {
        userService.replenishBalance(activeCustomer.getId(), amount);
        return SCHEMA_REDIRECT + redirect;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestAttribute CustomerViewDto activeCustomer,
            @RequestParam("tariff_id") long tariffId, @RequestParam(defaultValue = "/my_account") String redirect) {
        subscriptionService.subscribe(activeCustomer.getId(), tariffId);
        return SCHEMA_REDIRECT + redirect;
    }

    @GetMapping("/cancel_subscription") //TODO make it POST
    public String cancelSubscription(@RequestAttribute CustomerViewDto activeCustomer,
            @RequestParam("subscription_id") long subscriptionId, @RequestParam(defaultValue = "/my_account") String redirect) {
        subscriptionService.cancelSubscription(activeCustomer.getId(), subscriptionId);
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
            @RequestParam(defaultValue = "/login?updated") String redirect, HttpSession session) {
        if (!bindingResult.hasErrors()) {
            try {
                userService.updateCredentials(credentials);
                SecurityContextHolder.getContext().setAuthentication(null);
                return SCHEMA_REDIRECT + redirect;
            } catch (UnauthorizedException e) {
                bindingResult.rejectValue("currentPassword", "msg.user.wrong-password");
            }
        }
        return "my-account";
    }

    @InitBinder("credentials")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(credentialsValidator);
    }
}
