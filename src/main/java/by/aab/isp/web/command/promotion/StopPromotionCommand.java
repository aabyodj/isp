package by.aab.isp.web.command.promotion;

import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.PromotionService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

@Component
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
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof EmployeeDto;
    }
}
