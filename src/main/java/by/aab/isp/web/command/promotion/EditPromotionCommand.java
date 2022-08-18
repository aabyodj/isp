package by.aab.isp.web.command.promotion;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Promotion;
import by.aab.isp.entity.User;
import by.aab.isp.service.PromotionService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.ZoneId;

public class EditPromotionCommand extends Command {

    private final PromotionService promotionService;

    public EditPromotionCommand(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        long promotionId = 0;
        try {
            promotionId = Long.parseLong(req.getParameter("id"));
        } catch (Exception ignore) {
        }
        Promotion promotion = promotionService.getById(promotionId);
        req.setAttribute("promotion", promotion);
        if (promotion.getActiveSince() != null) {
            LocalDate since = LocalDate.ofInstant(promotion.getActiveSince(), ZoneId.systemDefault());
            req.setAttribute("activeSince", since);
        }
        if (promotion.getActiveUntil() != null) {
            LocalDate until = LocalDate.ofInstant(promotion.getActiveUntil(), ZoneId.systemDefault());
            req.setAttribute("activeUntil", until);
        }
        req.setAttribute("redirect", "?action=manage_promotions");  //TODO: determine a referer
        return "jsp/edit-promotion.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
