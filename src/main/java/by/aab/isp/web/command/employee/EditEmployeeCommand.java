package by.aab.isp.web.command.employee;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component("edit_employee")
public class EditEmployeeCommand extends Command {
    private final UserService userService;

    public EditEmployeeCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        String id = req.getParameter("id");
        Employee employee = userService.getEmployeeById(id != null ? Long.parseLong(id)
                                                                   : null);
        req.setAttribute("employee", employee);
        req.setAttribute("roles", Employee.Role.values());
        req.setAttribute("redirect", "?action=manage_employees");   //TODO: determine a referer
        return "jsp/edit-employee.jsp";
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
