package by.aab.isp.web.command;

import by.aab.isp.service.TariffService;
import jakarta.servlet.http.HttpServletRequest;

public class ShowAllTariffsCommand implements Command {

    private final TariffService tariffService;

    public ShowAllTariffsCommand(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        req.setAttribute("tariffs", tariffService.getAll());
        return "jsp/all-tariffs.jsp";
    }

}
