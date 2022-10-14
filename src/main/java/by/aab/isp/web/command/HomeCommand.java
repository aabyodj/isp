package by.aab.isp.web.command;

import by.aab.isp.dto.UserDto;
import by.aab.isp.entity.Promotion;
import by.aab.isp.entity.Tariff;
import by.aab.isp.service.PromotionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.FormatUtil;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class HomeCommand extends Command {

    private final PromotionService promotionService;
    private final TariffService tariffService;
    private final FormatUtil util;

    public HomeCommand(PromotionService promotionService, TariffService tariffService, FormatUtil util) {
        this.promotionService = promotionService;
        this.tariffService = tariffService;
		this.util = util;
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
            req.setAttribute("util", util);
        }
        return "jsp/index.jsp";
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return true;
    }

}
