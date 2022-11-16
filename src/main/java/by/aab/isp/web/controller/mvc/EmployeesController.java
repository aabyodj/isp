package by.aab.isp.web.controller.mvc;

import static by.aab.isp.service.security.AppUserDetails.ROLE_ADMIN;
import static by.aab.isp.web.Const.DEFAULT_EMPLOYEES_SORT;
import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
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

import by.aab.isp.repository.entity.Employee;
import by.aab.isp.service.UserService;
import by.aab.isp.service.dto.user.EmployeeEditDto;
import by.aab.isp.service.dto.user.EmployeeViewDto;
import by.aab.isp.service.validator.UserEditDtoValidator;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/employees")
@RolesAllowed(ROLE_ADMIN)
@RequiredArgsConstructor
public class EmployeesController {

    private final UserService userService;
    private final UserEditDtoValidator userValidator;
    private final MessageSource messageSource;

    @GetMapping
    public String viewAll(@RequestParam(name = "page", defaultValue = "1") int pageNumber, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, DEFAULT_EMPLOYEES_SORT);
        Page<EmployeeViewDto> employees = userService.getAllEmployees(request);
        model.addAttribute("page", employees);
        return "manage-employees";
    }

    @ModelAttribute("employee")
    public EmployeeEditDto initEmployeeEditDto() {
        return new EmployeeEditDto();
    }

    private static record RoleOption(String name, String label) {};

    @ModelAttribute("roles")
    public RoleOption[] provideRoles(Locale locale) {
        return Arrays.stream(Employee.Role.values())
                .map(role -> new RoleOption(role.name(), messageSource.getMessage(role.getMessageKey(), null, locale)))
                .toArray(RoleOption[]::new);
    }

    @ModelAttribute("redirect")
    public String getDefaultRedirect() {
        return "/employees";
    }

    @GetMapping("/new")
    public String createNewEmployee() {
        return "edit-employee";
    }

    @GetMapping("/{employeeId}")
    public String editEmployee(@PathVariable long employeeId, Model model) {
        EmployeeEditDto employee = userService.getEmployeeById(employeeId);
        model.addAttribute("employee", employee);
        return "edit-employee";
    }

    @PostMapping({"/new", "/{employeeId}"})
    public String saveEmployee(
            @Valid @ModelAttribute("employee") EmployeeEditDto employee, BindingResult bindingResult,
            @PathVariable(required = false) Long employeeId,
            @ModelAttribute String redirect) {
        if (!Objects.equals(employee.getId(), employeeId)) {
            throw new IllegalArgumentException();
        }
        if (null == employeeId && (null == employee.getPassword() || employee.getPassword().isBlank())) {
            bindingResult.rejectValue("password", "msg.validation.password.empty");
        }
        if (bindingResult.hasErrors()) {
            return "edit-employee";
        }
        userService.save(employee);   //TODO: terminate their session
        return SCHEMA_REDIRECT + redirect;
    }

    @PostMapping("/generate")
    public String generateEmployees(
            @RequestParam int quantity, @RequestParam(required = false) String active,
            @RequestParam String redirect) {
        userService.generateEmployees(quantity, active != null);
        return SCHEMA_REDIRECT + redirect;
    }

    @InitBinder("employee")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }
}
