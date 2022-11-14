package by.aab.isp.web.controller.mvc;

import static by.aab.isp.service.security.AppUserDetails.ROLE_ADMIN;
import static by.aab.isp.service.security.AppUserDetails.ROLE_MANAGER;
import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.DEFAULT_PROMOTIONS_SORT;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.util.Objects;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.service.Now;
import by.aab.isp.service.PromotionService;
import by.aab.isp.service.dto.promotion.PromotionEditDto;
import by.aab.isp.service.dto.promotion.PromotionViewDto;
import by.aab.isp.service.dto.user.EmployeeViewDto;
import by.aab.isp.service.validator.PromotionEditDtoValidator;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/promotions")
@RolesAllowed({ROLE_ADMIN, ROLE_MANAGER})
@RequiredArgsConstructor
public class PromotionsController {

    private final PromotionService promotionService;
    private final PromotionEditDtoValidator promotionValidator;

    @Autowired
    private Now now;

    @GetMapping
    @PermitAll
    public String viewAll(@RequestParam(name = "page", defaultValue = "1") int pageNumber,
            @RequestAttribute(required = false) EmployeeViewDto activeEmployee, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, DEFAULT_PROMOTIONS_SORT);
        Page<PromotionViewDto> promotions = activeEmployee != null ? promotionService.getAll(request)
                                                                   : promotionService.getActive(request);
        model.addAttribute("page", promotions);
        return "manage-promotions";
    }

    @ModelAttribute("redirect")
    @PermitAll
    public String getDefaultRedirect() {    //TODO get rid of this
        return "/promotions";
    }

    @GetMapping("/new")
    public String createNewPromotion(Model model) {
        model.addAttribute("promotion", PromotionEditDto.builder()
                .activeSince(now.getLocalDate())
                .build());
        return "edit-promotion";
    }

    @GetMapping("/{promotionId}")
    public String editPromotion(
            @PathVariable long promotionId, @RequestParam(required = false) String stop,
            @ModelAttribute String redirect, Model model) {
        if (null != stop) {
            promotionService.stop(promotionId);
            return SCHEMA_REDIRECT + redirect;
        }
        model.addAttribute("promotion", promotionService.getById(promotionId));
        return "edit-promotion";
    }

    @PostMapping({"/new", "/{promotionId}"})
    public String savePromotion(
            @PathVariable(required = false) Long promotionId,
            @Valid @ModelAttribute("promotion") PromotionEditDto promotion, BindingResult bindingResult,
            @ModelAttribute("redirect") String redirect) {
        if (!Objects.equals(promotionId, promotion.getId())) {
            throw new IllegalArgumentException();
        }
        if (bindingResult.hasErrors()) {
            return "edit-promotion";
        }
        promotion.setId(promotionId);
        promotionService.save(promotion);
        return SCHEMA_REDIRECT + redirect;
    }

    @InitBinder("promotion")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(promotionValidator);
    }

    @PostMapping("/generate")
    public String generatePromotions(
            @RequestParam int quantity, @RequestParam(required = false) String active,
            @ModelAttribute("redirect") String redirect) {
        promotionService.generatePromotions(quantity, active != null);
        return SCHEMA_REDIRECT + redirect;
    }
}
