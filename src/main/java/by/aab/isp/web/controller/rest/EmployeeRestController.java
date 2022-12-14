package by.aab.isp.web.controller.rest;

import static by.aab.isp.service.security.AppUserDetails.ROLE_ADMIN;
import static by.aab.isp.web.Const.DEFAULT_EMPLOYEES_SORT;
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
import by.aab.isp.service.dto.user.EmployeeEditDto;
import by.aab.isp.service.dto.user.EmployeeViewDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RolesAllowed(ROLE_ADMIN)
@RequiredArgsConstructor
public class EmployeeRestController {

    private static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(0, DEFAULT_PAGE_SIZE, DEFAULT_EMPLOYEES_SORT);

    private final UserService userService;

    @GetMapping
    public Page<EmployeeViewDto> getAll(@RequestBody(required = false) PageRequest request) {
        if (null == request) {
            request = DEFAULT_PAGE_REQUEST;
        }
        return userService.getAllEmployees(request);
    }

    @GetMapping("/{employeeId}")
    public EmployeeEditDto getEmployee(@PathVariable long employeeId) {
        return userService.getEmployeeById(employeeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeEditDto createEmployee(@Valid @RequestBody EmployeeEditDto employee) {
        employee.setId(null);
        return (EmployeeEditDto) userService.save(employee);
    }

    @PutMapping("/{employeeId}")
    public EmployeeEditDto updateEmployee(@PathVariable long employeeId,
            @Valid @RequestBody EmployeeEditDto employee) {
        employee.setId(employeeId);
        return (EmployeeEditDto) userService.save(employee);
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateEmployee(@PathVariable long employeeId) {
        userService.deactivate(employeeId);
    }
}
