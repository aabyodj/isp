package by.aab.isp.web.controller.rest;

import static by.aab.isp.service.security.AppUserDetails.ROLE_ADMIN;
import static by.aab.isp.service.security.AppUserDetails.ROLE_MANAGER;
import static by.aab.isp.web.Const.DEFAULT_CUSTOMERS_SORT;
import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import by.aab.isp.service.UserService;
import by.aab.isp.service.dto.user.CustomerEditDto;
import by.aab.isp.service.dto.user.CustomerViewDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RolesAllowed({ROLE_ADMIN, ROLE_MANAGER})
@RequiredArgsConstructor
public class CustomerRestController {

    private static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(0, DEFAULT_PAGE_SIZE, DEFAULT_CUSTOMERS_SORT);

    private final UserService userService;

    @GetMapping
    public Page<CustomerViewDto> getAll(@RequestBody(required = false) PageRequest request) {
        if (null == request) {
            request = DEFAULT_PAGE_REQUEST;
        }
        return userService.getAllCustomers(request);
    }

    @GetMapping("/{customerId}")
    public CustomerEditDto getCustomer(@PathVariable long customerId) {
        return userService.getCustomerById(customerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerEditDto createCustomer(@Valid @RequestBody CustomerEditDto customer) {   //TODO Customize the validation error message
        customer.setId(null);
        return (CustomerEditDto) userService.save(customer);
    }

    @PutMapping("/{customerId}")
    public CustomerEditDto updateCustomer(@PathVariable long customerId,
            @Valid @RequestBody CustomerEditDto customer) {
        customer.setId(customerId);
        return (CustomerEditDto) userService.save(customer);
    }

    @DeleteMapping("/{customerId}")
    public void deactivateCustomer(@PathVariable long customerId) {
        userService.deactivate(customerId);
    }
}
