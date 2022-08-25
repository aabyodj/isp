package by.aab.isp.web.command.employee;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.Pagination;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;

public class ManageEmployeesCommand extends Command {
    private final UserService userService;

    public ManageEmployeesCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Pagination pagination = new Pagination();
        pagination.setPageSize(DEFAULT_PAGE_SIZE);
        String page = req.getParameter("page");
        pagination.setPageNumber(page != null ? Integer.parseInt(page)
                                              : 0);
        req.setAttribute("employees", userService.getAllEmployees(pagination));
        req.setAttribute("pagination", pagination);
        return "jsp/manage-employees.jsp";
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
