package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import by.aab.isp.dto.user.EmployeeEditDto;
import by.aab.isp.dto.user.EmployeeViewDto;
import by.aab.isp.dto.user.UserViewDto;
import by.aab.isp.entity.Employee;
import by.aab.isp.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeesController {

    private static final Sort ORDER_BY_EMAIL = Sort.by("email");

    private final UserService userService;

    @GetMapping
    public String viewAll(@RequestAttribute EmployeeViewDto activeEmployee,
            @RequestParam(name = "page", defaultValue = "1") int pageNumber, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, ORDER_BY_EMAIL);
        Page<EmployeeViewDto> employees = userService.getAllEmployees(request);
        model.addAttribute("page", employees);
        return "manage-employees";
    }

    @GetMapping("/new")
    public String createNewEmployee(@RequestAttribute EmployeeViewDto activeEmployee,
            @RequestParam(defaultValue = "/employees") String redirect, Model model) {
        EmployeeEditDto employee = new EmployeeEditDto();
        model.addAttribute("employee", employee);
        model.addAttribute("roles", Employee.Role.values());
        if (null == redirect || redirect.isBlank()) {
            redirect = "/employees";
        }
        model.addAttribute("redirect", redirect);
        return "edit-employee";
    }

    @GetMapping("/{employeeId}")
    public String editEmployee(@RequestAttribute EmployeeViewDto activeEmployee, @PathVariable long employeeId,
            @RequestParam(required = false) String redirect, Model model) {
        EmployeeEditDto employee = userService.getEmployeeById(employeeId);
        model.addAttribute("employee", employee);
        model.addAttribute("roles", Employee.Role.values());
        if (null == redirect || redirect.isBlank()) {
            redirect = "/employees";
        }
        model.addAttribute("redirect", redirect);
        return "edit-employee";
    }

    @PostMapping
    public String saveEmployee(@RequestAttribute EmployeeViewDto activeEmployee,
            @RequestParam(required = false) Long id,
            @RequestParam String email,
            @RequestParam(name = "password1", required = false) String password,
            @RequestParam(name = "password2", required = false) String confirmPassword,
            @RequestParam(required = false) String active,
            @RequestParam Employee.Role role,
            @RequestParam String redirect) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new RuntimeException("Passwords do not match. Handler unimplemented"); //TODO: implement this
        }
        if (null != password && password.isBlank()) {
            password = null;
        }
        EmployeeEditDto employee = new EmployeeEditDto();
        employee.setId(id);
        employee.setEmail(email);
        employee.setPassword(password);
        employee.setActive(active != null);
        employee.setRole(role);
        userService.save(employee);   //TODO: terminate their session
        return SCHEMA_REDIRECT + redirect;
    }

    @PostMapping("/generate")
    public String generateEmployees(@RequestAttribute EmployeeViewDto activeEmployee,
            @RequestParam int quantity, @RequestParam(required = false) String active,
            @RequestParam String redirect) {
        userService.generateEmployees(quantity, active != null);
        return SCHEMA_REDIRECT + redirect;
    }

    @ExceptionHandler
    public String handleNonEmployee(ServletRequestBindingException e,
            @RequestAttribute(required = false) EmployeeViewDto activeEmployee,
            @RequestAttribute(required = false) UserViewDto activeUser, HttpServletRequest req)
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
