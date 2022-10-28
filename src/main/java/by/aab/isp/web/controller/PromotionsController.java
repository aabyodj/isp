package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.dto.promotion.PromotionDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.PromotionService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/promotion")
@RequiredArgsConstructor
public class PromotionsController {

    private static final Sort ORDER_BY_SINCE_THEN_BY_UNTIL = Sort.by("activeSince", "activeUntil");

    private final PromotionService promotionService;

    @GetMapping
    public String viewAll(@RequestParam(name = "page", defaultValue = "1") int pageNumber, @RequestAttribute(required = false) EmployeeDto activeEmployee, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, ORDER_BY_SINCE_THEN_BY_UNTIL);
        Page<PromotionDto> promotions = activeEmployee != null ? promotionService.getAll(request)
                                                               : promotionService.getActive(request);
        model.addAttribute("page", promotions);
        return "manage-promotions";
    }

    @GetMapping("/new")
    public String createNewPromotion(@RequestAttribute EmployeeDto activeEmployee, @RequestParam(required = false) String redirect, Model model) {
        model.addAttribute("promotion", promotionService.getById(null));
        if (null == redirect || redirect.isBlank()) {
            redirect = "/promotion";
        }
        model.addAttribute("redirect", redirect);
        return "edit-promotion";
    }

    @GetMapping("/{promotionId}")
    public String editPromotion(@RequestAttribute EmployeeDto activeEmployee, @PathVariable long promotionId, @RequestParam(required = false) String stop,
            @RequestParam(required = false) String redirect, Model model) {
        if (null == redirect || redirect.isBlank()) {
            redirect = "/promotion";
        }
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
            @RequestParam(required = false) Long id, @RequestParam String name, @RequestParam String description,
            @RequestParam(name = "active-since", required = false) String activeSince, @RequestParam(name = "active-until", required = false) String activeUntil,
            @RequestParam String redirect) {
        PromotionDto promotion = new PromotionDto();
        promotion.setId(id);
        promotion.setName(name);
        promotion.setDescription(description);
        if (activeSince != null && !activeSince.isBlank()) {
            promotion.setActiveSince(LocalDate.parse(activeSince).atStartOfDay());
        }
        if (activeUntil != null && !activeUntil.isBlank()) {
            promotion.setActiveUntil(LocalDate.parse(activeUntil).plusDays(1).atStartOfDay().minusNanos(1000));
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
