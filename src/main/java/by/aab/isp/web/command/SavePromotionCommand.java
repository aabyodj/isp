package by.aab.isp.web.command;

import by.aab.isp.entity.Promotion;
import by.aab.isp.service.PromotionService;
import jakarta.servlet.http.HttpServletRequest;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SavePromotionCommand implements Command {

    private final PromotionService promotionService;

    public SavePromotionCommand(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        Promotion promotion = new Promotion(
                Long.parseLong(req.getParameter("id")),
                req.getParameter("name"),
                req.getParameter("description")
        );
        promotionService.save(promotion);
        return SCHEMA_REDIRECT + req.getContextPath();
    }
}
