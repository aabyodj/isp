package by.aab.isp.web.command.promotion;

import by.aab.isp.dto.promotion.PromotionDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.PromotionService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;

@Component
public class ManagePromotionsCommand extends Command {

    private static final Sort ORDER_BY_SINCE_THEN_BY_UNTIL = Sort.by("activeSince", "activeUntil");

    private final PromotionService promotionService;

    public ManagePromotionsCommand(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        String pageStr = req.getParameter("page");
        int pageNumber = pageStr != null ? Integer.max(Integer.parseInt(pageStr) - 1, 0)
                                         : 0;
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, ORDER_BY_SINCE_THEN_BY_UNTIL);
        Page<PromotionDto> promotions = promotionService.getAll(request);
        req.setAttribute("page", promotions);
        return "jsp/manage-promotions.jsp";
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof EmployeeDto;
    }
}
