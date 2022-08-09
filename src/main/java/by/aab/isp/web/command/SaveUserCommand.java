package by.aab.isp.web.command;

import by.aab.isp.entity.CustomerAccount;
import by.aab.isp.entity.Tariff;
import by.aab.isp.entity.User;
import by.aab.isp.service.CustomerAccountService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SaveUserCommand implements Command {

    private final CustomerAccountService accountService;
    private final UserService userService;
    private final TariffService tariffService;

    public SaveUserCommand(CustomerAccountService accountService, UserService userService, TariffService tariffService) {
        this.accountService = accountService;
        this.userService = userService;
        this.tariffService = tariffService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        User user = new User(
                Long.parseLong(req.getParameter("id")),
                req.getParameter("email"),
                User.Role.valueOf(req.getParameter("role"))
        );
        userService.save(user);
        if (user.getRole() == User.Role.CUSTOMER) {
            CustomerAccount account = accountService.getByUser(user);
            long tariffId = Long.parseLong(req.getParameter("tariff"));
            if (tariffId != 0) {
                Tariff tariff = tariffService.getById(tariffId);
                account.setTariff(tariff);
            } else {
                account.setTariff(null);
            }
            accountService.save(account);
        }
        return SCHEMA_REDIRECT + "?action=manage_users";
    }
}
