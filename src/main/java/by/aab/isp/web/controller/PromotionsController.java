package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.dto.promotion.PromotionEditDto;
import by.aab.isp.dto.promotion.PromotionViewDto;
import by.aab.isp.dto.user.EmployeeViewDto;
import by.aab.isp.dto.user.UserViewDto;
import by.aab.isp.service.AccessDeniedException;
import by.aab.isp.service.Now;
import by.aab.isp.service.PromotionService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionsController {

    private static final Sort ORDER_BY_SINCE_THEN_BY_UNTIL = Sort.by("activeSince", "activeUntil");

    private final PromotionService promotionService;

    @Autowired
    private Now now;

    @GetMapping
    public String viewAll(@RequestParam(name = "page", defaultValue = "1") int pageNumber,
            @RequestAttribute(required = false) EmployeeViewDto activeEmployee, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, ORDER_BY_SINCE_THEN_BY_UNTIL);
        Page<PromotionViewDto> promotions = activeEmployee != null ? promotionService.getAll(request)
                                                                   : promotionService.getActive(request);
        model.addAttribute("page", promotions);
        return "manage-promotions";
    }

    @ModelAttribute("promotion")
    public PromotionEditDto initPromotion() {
        return PromotionEditDto.builder()
                .activeSince(now.getLocalDate())
                .build();
    }

    @ModelAttribute("redirect")
    public String getDefaultRedirect() {
        return "/promotions";
    }

    @GetMapping("/new")
    public String createNewPromotion(@RequestAttribute EmployeeViewDto activeEmployee) {
        return "edit-promotion";
    }

    @GetMapping("/{promotionId}")
    public String editPromotion(@RequestAttribute EmployeeViewDto activeEmployee,
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
    public String savePromotion(@RequestAttribute EmployeeViewDto activeEmployee,
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

    @PostMapping("/generate")
    public String generatePromotions(@RequestAttribute EmployeeViewDto activeEmployee,
            @RequestParam int quantity, @RequestParam(required = false) String active,
            @ModelAttribute("redirect") String redirect) {
        promotionService.generatePromotions(quantity, active != null);
        return SCHEMA_REDIRECT + redirect;
    }

    @ExceptionHandler
    public String handleNonEmployee(ServletRequestBindingException e,
            @RequestAttribute(required = false) EmployeeViewDto activeEmployee,
            @RequestAttribute(required = false) UserViewDto activeUser, HttpServletRequest req)
            throws ServletRequestBindingException, UnsupportedEncodingException {
        if (activeEmployee != null) {
            throw e;
        }
        if (activeUser != null) {
            throw new AccessDeniedException();
        }
        String redirect = URLEncoder.encode(req.getRequestURL().toString(), "UTF-8");
        return SCHEMA_REDIRECT + "/login?redirect=" + redirect;
    }
}
