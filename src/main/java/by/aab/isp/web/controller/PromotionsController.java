package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.Now;
import by.aab.isp.service.PromotionService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/promotion")
@RequiredArgsConstructor
public class PromotionsController {

    private static final Sort ORDER_BY_SINCE_THEN_BY_UNTIL = Sort.by("activeSince", "activeUntil");

    private final PromotionService promotionService;

    @Autowired
    private Now now;

    @GetMapping
    public String viewAll(@RequestParam(name = "page", defaultValue = "1") int pageNumber, @RequestAttribute(required = false) EmployeeDto activeEmployee, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, ORDER_BY_SINCE_THEN_BY_UNTIL);
        Page<PromotionViewDto> promotions = activeEmployee != null ? promotionService.getAll(request)
                                                                   : promotionService.getActive(request);
        model.addAttribute("page", promotions);
        return "manage-promotions";
    }

    @GetMapping("/new")
    public String createNewPromotion(@RequestAttribute EmployeeDto activeEmployee, @RequestParam(defaultValue = "/promotion") String redirect, Model model) {
        model.addAttribute("promotion", PromotionEditDto.builder()
                .activeSince(now.getLocalDate())
                .build());
        model.addAttribute("redirect", redirect);
        return "edit-promotion";
    }

    @GetMapping("/{promotionId}")
    public String editPromotion(@RequestAttribute EmployeeDto activeEmployee, @PathVariable long promotionId, @RequestParam(required = false) String stop,
            @RequestParam(defaultValue = "/promotion") String redirect, Model model) {
        if (null != stop) {
            promotionService.stop(promotionId);
            return SCHEMA_REDIRECT + redirect;
        }
        model.addAttribute("promotion", promotionService.getById(promotionId));
        model.addAttribute("redirect", redirect);
        return "edit-promotion";
    }

    @PostMapping
    public String savePromotion(@RequestAttribute EmployeeDto activeEmployee,
            @Valid @ModelAttribute("promotion") PromotionEditDto promotion, BindingResult bindingResult,
            @ModelAttribute("redirect") String redirect) {
        if (bindingResult.hasErrors()) {
            return "edit-promotion";
        }
        promotionService.save(promotion);
        return SCHEMA_REDIRECT + redirect;
    }

    @PostMapping("/generate")
    public String generatePromotions(@RequestAttribute EmployeeDto activeEmployee, @RequestParam int quantity, @RequestParam(required = false) String active,
            @RequestParam String redirect) {
        promotionService.generatePromotions(quantity, active != null);
        return SCHEMA_REDIRECT + redirect;
    }

    @ExceptionHandler
    public String handleNonEmployee(ServletRequestBindingException e,
            @RequestAttribute(required = false) EmployeeDto activeEmployee, @RequestAttribute(required = false) UserDto activeUser, HttpServletRequest req)
            throws ServletRequestBindingException, UnsupportedEncodingException {
        if (activeEmployee != null) {
            throw e;
        }
        if (activeUser != null) {
            throw new RuntimeException("Access denied");    //TODO: create a custom handler
        }
        String redirect = URLEncoder.encode(req.getRequestURL().toString(), "UTF-8");
        return SCHEMA_REDIRECT + "/login?redirect=" + redirect;
    }
}
