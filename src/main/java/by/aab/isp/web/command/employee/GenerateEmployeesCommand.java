package by.aab.isp.web.command.employee;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

@Component
public class GenerateEmployeesCommand extends Command {

    private final UserService userService;

    public GenerateEmployeesCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        boolean active = req.getParameter("active") != null;
        userService.generateEmployees(quantity, active);
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
