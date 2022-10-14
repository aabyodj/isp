package by.aab.isp.web.command.employee;

import by.aab.isp.dto.EmployeeDto;
import by.aab.isp.dto.UserDto;
import by.aab.isp.entity.Employee;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class EditEmployeeCommand extends Command {
    private final UserService userService;

    public EditEmployeeCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        String id = req.getParameter("id");
        EmployeeDto employee = userService.getEmployeeById(id != null ? Long.parseLong(id)
                                                                      : null);
        req.setAttribute("employee", employee);
        req.setAttribute("roles", Employee.Role.values());
        req.setAttribute("redirect", "?action=manage_employees");   //TODO: determine a referer
        return "jsp/edit-employee.jsp";
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
