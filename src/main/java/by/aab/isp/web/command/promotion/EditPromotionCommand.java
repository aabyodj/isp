package by.aab.isp.web.command.promotion;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Promotion;
import by.aab.isp.entity.User;
import by.aab.isp.service.PromotionService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class EditPromotionCommand extends Command {

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
                                               : new Promotion();   //TODO: get this from promotionService
        req.setAttribute("promotion", promotion);
        req.setAttribute("redirect", "?action=manage_promotions");  //TODO: determine a referer
        return "jsp/edit-promotion.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
