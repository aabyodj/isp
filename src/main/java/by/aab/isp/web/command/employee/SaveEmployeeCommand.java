package by.aab.isp.web.command.employee;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SaveEmployeeCommand extends Command {
    private final UserService userService;

    public SaveEmployeeCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        Employee employee = new Employee();
        employee.setId(Long.parseLong(req.getParameter("id")));
        employee.setEmail(req.getParameter("email"));
        employee.setRole(Employee.Role.valueOf(req.getParameter("role")));
        userService.save(employee);
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(User user) {
        if (!(user instanceof Employee)) return false;
        Employee employee = (Employee) user;
        return employee.getRole() == Employee.Role.ADMIN;
    }
}
