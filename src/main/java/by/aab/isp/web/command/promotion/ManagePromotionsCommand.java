package by.aab.isp.web.command.promotion;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Promotion;
import by.aab.isp.entity.User;
import by.aab.isp.service.PromotionService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class ManagePromotionsCommand extends Command {
    private final PromotionService promotionService;

    public ManagePromotionsCommand(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Iterable<Promotion> promotions = promotionService.getAll();
        if (promotions.spliterator().estimateSize() > 0) {
            req.setAttribute("promotions", promotions);
        }
        return "jsp/manage-promotions.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
