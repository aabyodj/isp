package by.aab.isp.web.command.customer;

import by.aab.isp.dto.user.CustomerDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;

@Component
public class ManageCustomersCommand extends Command {

    private static final Sort ORDER_BY_EMAIL = Sort.by("email");

    private final UserService userService;

    public ManageCustomersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        String pageStr = req.getParameter("page");
        int pageNumber = pageStr != null ? Integer.max(Integer.parseInt(pageStr) - 1, 0)
                                         : 0;
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, ORDER_BY_EMAIL);
        Page<CustomerDto> customers = userService.getAllCustomers(request);
        req.setAttribute("page", customers);
        return "jsp/manage-customers.jsp";
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof EmployeeDto;
    }
}
