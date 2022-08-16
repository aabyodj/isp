package by.aab.isp.web.command.tariff;

import by.aab.isp.entity.Tariff;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class EditTariffCommand implements Command {

    private final TariffService tariffService;

    public EditTariffCommand(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        long tariffId = 0;
        try {
            tariffId = Long.parseLong(req.getParameter("id"));
        } catch (Exception ignore) {
        }
        Tariff tariff = tariffId != 0 ? tariffService.getById(tariffId)
                                      : new Tariff();
        req.setAttribute("tariff", tariff);
        return "jsp/edit-tariff.jsp";
    }

}
