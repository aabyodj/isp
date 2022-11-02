package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.dto.subscription.SubscriptionDto;
import by.aab.isp.dto.tariff.TariffViewDto;
import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomersController {

    private static final Sort ORDER_BY_EMAIL = Sort.by("email");

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;

    @GetMapping
    public String viewAll(@RequestAttribute EmployeeDto activeEmployee, @RequestParam(name = "page", defaultValue = "1") int pageNumber, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, ORDER_BY_EMAIL);
        Page<CustomerDto> customers = userService.getAllCustomers(request);
        model.addAttribute("page", customers);
        return "manage-customers";
    }

    @GetMapping("/new")
    public String createNewCustomer(@RequestAttribute EmployeeDto activeEmployee, @RequestParam(defaultValue = "/customers") String redirect, Model model) {
        model.addAttribute("customer", userService.getCustomerById(null));
        model.addAttribute("tariffs", tariffService.getActive());
        model.addAttribute("redirect", redirect);
        return "edit-customer";
    }

    @GetMapping("/{customerId}")
    public String editCustomer(@RequestAttribute EmployeeDto activeEmployee, @PathVariable long customerId,
            @RequestParam(defaultValue = "/customers") String redirect, Model model) {
        model.addAttribute("customer", userService.getCustomerById(customerId));
        TariffViewDto activeTariff = subscriptionService.getActiveSubscriptions(customerId)
                .stream()
                .map(SubscriptionDto::getTariff)
                .findAny()
                .orElse(null);
        model.addAttribute("activeTariff", activeTariff);
        model.addAttribute("tariffs", tariffService.getActive());
        model.addAttribute("redirect", redirect);
        return "edit-customer";
    }

    @PostMapping
    public String saveCustomer(@RequestAttribute EmployeeDto activeEmployee,
            @RequestParam(required = false) Long id,
            @RequestParam String email,
            @RequestParam(name = "password1", required = false) String password,
            @RequestParam(name = "password2", required = false) String confirmPassword,
            @RequestParam(required = false) String active,
            @RequestParam BigDecimal balance,
            @RequestParam("permitted-overdraft") BigDecimal permittedOverdraft,
            @RequestParam(name = "payoff-date", required = false) String payoffDate,
            @RequestParam String tariff,
            @RequestParam String redirect) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new RuntimeException("Passwords do not match. Handler unimplemented"); //TODO: implement this
        }
        if (null != password && password.isBlank()) {
            password = null;
        }
        CustomerDto customer = new CustomerDto();
        customer.setId(id);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setActive(active != null);
        customer.setBalance(balance);
        customer.setPermittedOverdraft(permittedOverdraft);
        if (payoffDate != null && !payoffDate.isBlank()) {
            customer.setPayoffDate(LocalDate.parse(payoffDate).plusDays(1).atStartOfDay().minusNanos(1000));
        }
        customer = (CustomerDto) userService.save(customer);   //TODO: terminate their session
        Long tariffId = !tariff.equals("none") ? Long.parseLong(tariff)
                                               : null;
        subscriptionService.setOneTariffForCustomer(customer.getId(), tariffId);    //TODO: use extended DTO for this
        return SCHEMA_REDIRECT + redirect;
    }

    @PostMapping("/generate")
    public String generatePromotions(@RequestAttribute EmployeeDto activeEmployee, @RequestParam int quantity, @RequestParam(required = false) String active,
            @RequestParam String redirect) {
        userService.generateCustomers(quantity, active != null);
        return SCHEMA_REDIRECT + redirect;
    }

    @ExceptionHandler
    public String handleNonEmployee(ServletRequestBindingException e,
            @RequestAttribute(required = false) EmployeeDto activeEmployee, @RequestAttribute(required = false) UserDto activeUser, HttpServletRequest req)
            throws ServletRequestBindingException, UnsupportedEncodingException {
        if (activeEmployee != null) {
            throw e;
        }
        if (activeUser != null) {
            throw new RuntimeException("Access denied");    //TODO: create a custom handler
        }
        String redirect = URLEncoder.encode(req.getRequestURL().toString(), "UTF-8");
        return SCHEMA_REDIRECT + "/login?redirect=" + redirect;
    }
}
