package by.aab.isp.web.command.employee;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class EditEmployeeCommand extends Command {
    private final UserService userService;

    public EditEmployeeCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        long employeeId = 0;
        try {
            employeeId = Long.parseLong(req.getParameter("id"));
        } catch (Exception ignore) {
        }
        Employee employee = userService.getEmployeeById(employeeId);
        req.setAttribute("employee", employee);
        req.setAttribute("roles", Employee.Role.values());
        req.setAttribute("redirect", "?action=manage_employees");   //TODO: determine a referer
        return "jsp/edit-employee.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        if (!(user instanceof Employee)) return false;
        Employee employee = (Employee) user;
        return employee.getRole() == Employee.Role.ADMIN;
    }
}
