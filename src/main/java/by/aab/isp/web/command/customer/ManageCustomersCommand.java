package by.aab.isp.web.command.customer;

import by.aab.isp.dto.EmployeeDto;
import by.aab.isp.dto.UserDto;
import by.aab.isp.entity.Customer;
import by.aab.isp.service.Pagination;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;

@Component
public class ManageCustomersCommand extends Command {

    private final UserService userService;

    public ManageCustomersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Pagination pagination = new Pagination();
        pagination.setPageSize(DEFAULT_PAGE_SIZE);
        String page = req.getParameter("page");
        pagination.setPageNumber(page != null ? Integer.parseInt(page)
                                      : 0);
        Iterable<Customer> customers = userService.getAllCustomers(pagination);
        if (customers.spliterator().estimateSize() > 0) {
            req.setAttribute("customers", customers);
        }
        req.setAttribute("pagination", pagination);
        return "jsp/manage-customers.jsp";
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof EmployeeDto;
    }
}
