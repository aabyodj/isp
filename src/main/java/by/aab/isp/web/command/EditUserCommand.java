package by.aab.isp.web.command;

import by.aab.isp.entity.User;
import by.aab.isp.service.CustomerAccountService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

public class EditUserCommand implements Command {

    private final CustomerAccountService accountService;
    private final UserService userService;
    private final TariffService tariffService;

    public EditUserCommand(CustomerAccountService accountService, UserService userService, TariffService tariffService) {
        this.accountService = accountService;
        this.userService = userService;
        this.tariffService = tariffService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        long userId = 0;
        try {
            userId = Long.parseLong(req.getParameter("id"));
        } catch (Exception ignore) {
        }
        User user = userId != 0 ? userService.getById(userId)
                                : new User();
        req.setAttribute("user", user);
        req.setAttribute("userRoles", User.Role.values());
        if (user.getRole() == User.Role.CUSTOMER) {
            req.setAttribute("account", accountService.getByUser(user));
            req.setAttribute("tariffs", tariffService.getAll());
        }
        return "jsp/edit-user.jsp";
    }
}
