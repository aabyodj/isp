package by.aab.isp.web.controller.mvc;

import static by.aab.isp.web.Const.DEFAULT_CUSTOMERS_SORT;
import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import by.aab.isp.service.dto.subscription.SubscriptionViewDto;
import by.aab.isp.service.dto.tariff.TariffViewDto;
import by.aab.isp.service.dto.user.CustomerEditDto;
import by.aab.isp.service.dto.user.CustomerViewDto;
import by.aab.isp.service.validator.UserEditDtoValidator;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomersController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final TariffService tariffService;
    private final UserEditDtoValidator userValidator;

    @GetMapping
    public String viewAll(@RequestParam(name = "page", defaultValue = "1") int pageNumber, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, DEFAULT_CUSTOMERS_SORT);
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
    public String createNewCustomer() {
        return "edit-customer";
    }

    @GetMapping("/{customerId}")
    public String editCustomer(@PathVariable long customerId, Model model) {
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
    public String saveCustomer(
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
    public String generatePromotions(@RequestParam int quantity, @RequestParam(required = false) String active,
            @RequestParam String redirect) {
        userService.generateCustomers(quantity, active != null);
        return SCHEMA_REDIRECT + redirect;
    }

    @InitBinder("customer")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }
}
