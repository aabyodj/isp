package by.aab.isp.web.command;

import by.aab.isp.entity.Employee;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SaveEmployeeCommand implements Command {
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
        return SCHEMA_REDIRECT + "?action=manage_employees";
    }
}
