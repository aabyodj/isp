package by.aab.isp.web.command.employee;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import java.util.Objects;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

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
        Employee employee = new Employee();
        employee.setId(id);
        employee.setEmail(req.getParameter("email"));
        employee.setActive(req.getParameter("active") != null);
        employee.setRole(Employee.Role.valueOf(req.getParameter("role")));
        userService.save(employee, password);   //TODO: terminate their session
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(User user) {
        if (!(user instanceof Employee)) {
            return false;
        }
        Employee employee = (Employee) user;
        return employee.getRole() == Employee.Role.ADMIN;
    }
}
