package by.aab.isp.web.command.promotion;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Promotion;
import by.aab.isp.entity.User;
import by.aab.isp.service.Pagination;
import by.aab.isp.service.PromotionService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;

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
        Iterable<Promotion> promotions = promotionService.getAll(pagination);
        if (promotions.spliterator().estimateSize() > 0) {
            req.setAttribute("promotions", promotions);
            req.setAttribute("now", LocalDateTime.now());
        }
        req.setAttribute("pagination", pagination);
        return "jsp/manage-promotions.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
