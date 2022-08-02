package by.aab.isp.web.command;

import by.aab.isp.service.TariffService;
import jakarta.servlet.http.HttpServletRequest;

public class HomeCommand implements Command {

    private final TariffService tariffService;

    public HomeCommand(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        req.setAttribute("tariffs", tariffService.getAll());
        return "jsp/index.jsp";
    }

}
