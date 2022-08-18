package by.aab.isp.web.command.promotion;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.User;
import by.aab.isp.service.PromotionService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class StopPromotionCommand extends Command {

    private final PromotionService promotionService;

    public StopPromotionCommand(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        long promotionId = Long.parseLong(req.getParameter("id"));
        promotionService.stop(promotionId);
        return SCHEMA_REDIRECT + "?action=manage_promotions";   //TODO: determine referer
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
