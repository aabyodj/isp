package by.aab.isp.web.command;

import by.aab.isp.service.TariffService;
import jakarta.servlet.http.HttpServletRequest;

public class ShowTariffCommand implements Command {

    private final TariffService tariffService;

    public ShowTariffCommand(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        long id = Long.parseLong(req.getParameter("id"));
        req.setAttribute("tariff", tariffService.getById(id));
        return "jsp/one-tariff.jsp";
    }

}
