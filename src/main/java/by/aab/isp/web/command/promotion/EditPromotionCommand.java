package by.aab.isp.web.command.promotion;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Promotion;
import by.aab.isp.entity.User;
import by.aab.isp.service.PromotionService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

public class EditPromotionCommand extends Command {

    private final PromotionService promotionService;

    public EditPromotionCommand(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        String id = req.getParameter("id");
        Promotion promotion = promotionService.getById(id != null ? Long.parseLong(id)
                                                                  : null);
        req.setAttribute("promotion", promotion);
        req.setAttribute("redirect", "?action=manage_promotions");  //TODO: determine a referer
        return "jsp/edit-promotion.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
