package by.aab.isp.web.command.tariff;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Tariff;
import by.aab.isp.entity.User;
import by.aab.isp.service.TariffService;
import jakarta.servlet.http.HttpServletRequest;

public class ManageTariffsCommand extends by.aab.isp.web.command.Command {

    private final TariffService tariffService;

    public ManageTariffsCommand(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Iterable<Tariff> tariffs = tariffService.getAll();
        if (tariffs.spliterator().estimateSize() > 0) {
            req.setAttribute("tariffs", tariffs);
        }
        return "jsp/manage-tariffs.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
