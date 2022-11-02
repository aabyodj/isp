package by.aab.isp.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import by.aab.isp.dto.promotion.PromotionViewDto;
import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.service.PromotionService;
import by.aab.isp.service.TariffService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PromotionService promotionService;
    private final TariffService tariffService;

    @GetMapping("/")
    public String getHomepage(Model model) {
        List<PromotionViewDto> promotions = promotionService.getForHomepage();
        if (!promotions.isEmpty()) {
            model.addAttribute("promotions", promotions);
        }
        List<ShowTariffDto> tariffs = tariffService.getForHomepage();
        if (!tariffs.isEmpty()) {
            model.addAttribute("tariffs", tariffs);
        }
        return "index";
    }

}
