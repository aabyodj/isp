package by.aab.isp.web.command.promotion;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Promotion;
import by.aab.isp.entity.User;
import by.aab.isp.service.PromotionService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import java.time.*;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SavePromotionCommand extends Command {

    private final PromotionService promotionService;

    public SavePromotionCommand(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Promotion promotion = new Promotion();
        promotion.setId(Long.parseLong(req.getParameter("id")));
        promotion.setName(req.getParameter("name"));
        promotion.setDescription(req.getParameter("description"));
        String since = req.getParameter("active-since");
        if (since != null && !since.isBlank()) {
            promotion.setActiveSince(LocalDate.parse(since).atStartOfDay());
        }
        String until = req.getParameter("active-until");
        if (until != null && !until.isBlank()) {
            LocalDateTime activeUntil = LocalDate.parse(until).plusDays(1).atStartOfDay().minusNanos(1000);
            promotion.setActiveUntil(activeUntil);
        }
        promotionService.save(promotion);
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
