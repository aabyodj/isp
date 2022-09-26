package by.aab.isp.web.command;

import by.aab.isp.entity.Promotion;
import by.aab.isp.entity.Tariff;
import by.aab.isp.entity.User;
import by.aab.isp.service.PromotionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.Util;
import javax.servlet.http.HttpServletRequest;

public class HomeCommand extends Command {

    private final PromotionService promotionService;
    private final TariffService tariffService;

    public HomeCommand(PromotionService promotionService, TariffService tariffService) {
        this.promotionService = promotionService;
        this.tariffService = tariffService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Iterable<Promotion> promotions = promotionService.getForHomepage();
        if (promotions.spliterator().estimateSize() > 0) {
            req.setAttribute("promotions", promotions);
        }
        Iterable<Tariff> tariffs = tariffService.getForHomepage();
        if (tariffs.spliterator().estimateSize() > 0) {
            req.setAttribute("tariffs", tariffs);
            req.setAttribute("util", Util.getInstance());
        }
        return "jsp/index.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return true;
    }

}
