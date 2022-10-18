package by.aab.isp.web.command.promotion;

import by.aab.isp.dto.promotion.PromotionDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.Pagination;
import by.aab.isp.service.PromotionService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import java.util.List;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;

@Component
public class ManagePromotionsCommand extends Command {

    private final PromotionService promotionService;

    public ManagePromotionsCommand(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Pagination pagination = new Pagination();
        pagination.setPageSize(DEFAULT_PAGE_SIZE);
        String page = req.getParameter("page");
        pagination.setPageNumber(page != null ? Integer.parseInt(page)
                                              : 0);
        List<PromotionDto> promotions = promotionService.getAll(pagination);
        if (!promotions.isEmpty()) {
            req.setAttribute("promotions", promotions);
        }
        req.setAttribute("pagination", pagination);
        return "jsp/manage-promotions.jsp";
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof EmployeeDto;
    }
}
