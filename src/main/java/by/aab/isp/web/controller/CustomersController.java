package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.dto.subscription.SubscriptionViewDto;
import by.aab.isp.dto.tariff.TariffViewDto;
import by.aab.isp.dto.user.CustomerEditDto;
import by.aab.isp.dto.user.CustomerViewDto;
import by.aab.isp.dto.user.EmployeeViewDto;
import by.aab.isp.dto.user.UserViewDto;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import by.aab.isp.validator.UserEditDtoValidator;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomersController {

    private static final Sort ORDER_BY_EMAIL = Sort.by("email");

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;
    private final UserEditDtoValidator userValidator;

    @GetMapping
    public String viewAll(@RequestAttribute EmployeeViewDto activeEmployee, @RequestParam(name = "page", defaultValue = "1") int pageNumber, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, ORDER_BY_EMAIL);
        Page<CustomerViewDto> customers = userService.getAllCustomers(request);
        model.addAttribute("page", customers);
        return "manage-customers";
    }

    @ModelAttribute("customer")
    public CustomerEditDto initCustomer() {
        return CustomerEditDto.builder()
                .balance(BigDecimal.ZERO)
                .permittedOverdraft(BigDecimal.ZERO)
                .build();
    }

    @ModelAttribute("tariffs")
    public List<TariffViewDto> getActiveTariffs() {
        return tariffService.getActive();
    }

    @ModelAttribute("redirect")
    public String getDefaultRedirect() {
        return "/customers";
    }

    @GetMapping("/new")
    public String createNewCustomer(@RequestAttribute EmployeeViewDto activeEmployee) {
        return "edit-customer";
    }

    @GetMapping("/{customerId}")
    public String editCustomer(@RequestAttribute EmployeeViewDto activeEmployee,
            @PathVariable long customerId, Model model) {
        model.addAttribute("customer", userService.getCustomerById(customerId));
        TariffViewDto activeTariff = subscriptionService.getActiveSubscriptions(customerId)
                .stream()
                .map(SubscriptionViewDto::getTariff)
                .findAny()
                .orElse(null);
        model.addAttribute("activeTariff", activeTariff);
        return "edit-customer";
    }

    @PostMapping({"/new", "/{customerId}"})
    public String saveCustomer(@RequestAttribute EmployeeViewDto activeEmployee,
            @Valid @ModelAttribute("customer") CustomerEditDto customer, BindingResult bindingResult,
            @PathVariable(required = false) Long customerId,
            @RequestParam String tariff, @ModelAttribute("redirect") String redirect) {
        if (!Objects.equals(customer.getId(), customerId)) {
            throw new IllegalArgumentException();
        }
        if (null == customerId && (null == customer.getPassword() || customer.getPassword().isBlank())) {
            bindingResult.rejectValue("password", "msg.validation.password.empty");
        }
        if (bindingResult.hasErrors()) {
            return "edit-customer";
        }
        customer = (CustomerEditDto) userService.save(customer);   //TODO: terminate their session
        Long tariffId = !tariff.equals("none") ? Long.parseLong(tariff)
                                               : null;
        subscriptionService.setOneTariffForCustomer(customer.getId(), tariffId);    //TODO: use extended DTO for this
        return SCHEMA_REDIRECT + redirect;
    }

    @PostMapping("/generate")
    public String generatePromotions(@RequestAttribute EmployeeViewDto activeEmployee, @RequestParam int quantity, @RequestParam(required = false) String active,
            @RequestParam String redirect) {
        userService.generateCustomers(quantity, active != null);
        return SCHEMA_REDIRECT + redirect;
    }

    @InitBinder("customer")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @ExceptionHandler
    public String handleNonEmployee(ServletRequestBindingException e,
            @RequestAttribute(required = false) EmployeeViewDto activeEmployee, @RequestAttribute(required = false) UserViewDto activeUser, HttpServletRequest req)
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
