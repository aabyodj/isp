package by.aab.isp.web.command.employee;

import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.entity.Employee;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import java.util.Objects;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

@Component
public class SaveEmployeeCommand extends Command {
    private final UserService userService;

    public SaveEmployeeCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        String idString = req.getParameter("id");
        Long id = idString != null && !idString.isBlank() ? Long.parseLong(idString)
                                                          : null;
        String password = req.getParameter("password1");
        if (!Objects.equals(password, req.getParameter("password2"))) {
            throw new RuntimeException("Passwords do not match. Handler unimplemented"); //TODO: implement this
        }
        if (null != password && password.isBlank()) {
            password = null;
        }
        EmployeeDto employee = new EmployeeDto();
        employee.setId(id);
        employee.setEmail(req.getParameter("email"));
        employee.setPassword(password);
        employee.setActive(req.getParameter("active") != null);
        employee.setRole(Employee.Role.valueOf(req.getParameter("role")));
        userService.save(employee);   //TODO: terminate their session
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        if (!(user instanceof EmployeeDto)) {
            return false;
        }
        EmployeeDto employee = (EmployeeDto) user;
        return employee.getRole() == Employee.Role.ADMIN;
    }
}
