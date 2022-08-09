package by.aab.isp.web.command;

import by.aab.isp.service.PromotionService;
import by.aab.isp.service.TariffService;
import jakarta.servlet.http.HttpServletRequest;

public class HomeCommand implements Command {

    private final PromotionService promotionService;
    private final TariffService tariffService;

    public HomeCommand(PromotionService promotionService, TariffService tariffService) {
        this.promotionService = promotionService;
        this.tariffService = tariffService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        req.setAttribute("promotions", promotionService.getAll());
        req.setAttribute("tariffs", tariffService.getAll());
        return "jsp/index.jsp";
    }

}
