package by.aab.isp.web.command;

import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.entity.Promotion;
import by.aab.isp.service.PromotionService;
import by.aab.isp.service.TariffService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HomeCommand extends Command {

    private final PromotionService promotionService;
    private final TariffService tariffService;

    @Override
    public String execute(HttpServletRequest req) {
        Iterable<Promotion> promotions = promotionService.getForHomepage();
        if (promotions.spliterator().estimateSize() > 0) {
            req.setAttribute("promotions", promotions);
        }
        List<ShowTariffDto> tariffs = tariffService.getForHomepage();
        if (!tariffs.isEmpty()) {
            req.setAttribute("tariffs", tariffs);
        }
        return "jsp/index.jsp";
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return true;
    }

}
