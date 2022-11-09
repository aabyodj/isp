package by.aab.isp.web.controller.mvc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import by.aab.isp.service.PromotionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.dto.promotion.PromotionViewDto;
import by.aab.isp.service.dto.tariff.TariffViewDto;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private static final Sort ORDER_PROMOTIONS_BY_SINCE_REVERSED_THEN_BY_UNTIL = Sort.by("activeSince").descending().and(Sort.by("activeUntil"));
    private static final Sort ORDER_TARIFFS_BY_PRICE = Sort.by("price");

    private final PromotionService promotionService;
    private final TariffService tariffService;

    @GetMapping("/")
    public String getHomepage(@Value("${homepage.promotions-count}") Integer promotionsCount,
            @Value("${homepage.tariffs-count}") Integer tariffsCount,
            Model model) {
        PageRequest request = PageRequest.of(0, promotionsCount, ORDER_PROMOTIONS_BY_SINCE_REVERSED_THEN_BY_UNTIL);
        Page<PromotionViewDto> promotions = promotionService.getActive(request);
        model.addAttribute("promotions", promotions);
        request = PageRequest.of(0, tariffsCount, ORDER_TARIFFS_BY_PRICE);
        Page<TariffViewDto> tariffs = tariffService.getActive(request);
        model.addAttribute("tariffs", tariffs);
        return "index";
    }

}
