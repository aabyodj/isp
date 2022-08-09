package by.aab.isp.web.command;

import by.aab.isp.entity.Promotion;
import by.aab.isp.service.PromotionService;
import jakarta.servlet.http.HttpServletRequest;

public class EditPromotionCommand implements Command {

    private final PromotionService promotionService;

    public EditPromotionCommand(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        long promotionId = 0;
        try {
            promotionId = Long.parseLong(req.getParameter("id"));
        } catch (Exception ignore) {
        }
        Promotion promotion = promotionId != 0 ? promotionService.getById(promotionId)
                                               : new Promotion();
        req.setAttribute("promotion", promotion);
        return "jsp/edit-promotion.jsp";
    }
}
