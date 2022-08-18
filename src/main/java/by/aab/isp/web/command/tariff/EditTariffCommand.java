package by.aab.isp.web.command.tariff;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Tariff;
import by.aab.isp.entity.User;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class EditTariffCommand extends Command {

    private final TariffService tariffService;

    public EditTariffCommand(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        long tariffId = 0;
        try {
            tariffId = Long.parseLong(req.getParameter("id"));
        } catch (Exception ignore) {
        }
        Tariff tariff = tariffService.getById(tariffId);
        req.setAttribute("tariff", tariff);
        req.setAttribute("redirect", "?action=manage_tariffs"); //TODO: determine a referer
        return "jsp/edit-tariff.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }

}
