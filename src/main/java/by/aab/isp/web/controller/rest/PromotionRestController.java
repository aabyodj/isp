package by.aab.isp.web.controller.rest;

import static by.aab.isp.service.security.AppUserDetails.ROLE_ADMIN;
import static by.aab.isp.service.security.AppUserDetails.ROLE_MANAGER;
import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.DEFAULT_PROMOTIONS_SORT;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import by.aab.isp.service.PromotionService;
import by.aab.isp.service.dto.promotion.PromotionEditDto;
import by.aab.isp.service.dto.promotion.PromotionViewDto;
import by.aab.isp.service.dto.user.EmployeeViewDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/promotions")
@RolesAllowed({ROLE_ADMIN, ROLE_MANAGER})
@RequiredArgsConstructor
public class PromotionRestController {

    private static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(0, DEFAULT_PAGE_SIZE, DEFAULT_PROMOTIONS_SORT);
    private final PromotionService promotionService;

    @GetMapping
    @PermitAll
    public Page<PromotionViewDto> getAll(@RequestBody(required = false) PageRequest request, 
            @RequestAttribute(required = false) EmployeeViewDto activeEmployee) {
        if (null == request) {
            request = DEFAULT_PAGE_REQUEST;
        }
        return activeEmployee != null ? promotionService.getAll(request)
                                      : promotionService.getActive(request);
    }

    @GetMapping("/{promotionId}")
    public PromotionEditDto getPromotion(@PathVariable long promotionId) {
        return promotionService.getById(promotionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PromotionEditDto createPromotion(@Valid @RequestBody PromotionEditDto promotion) {
        promotion.setId(null);
        return promotionService.save(promotion);
    }

    @PutMapping("/{promotionId}")
    public PromotionEditDto updatePromotion(@PathVariable long promotionId, @Valid @RequestBody PromotionEditDto promotion) {
        promotion.setId(promotionId);
        return promotionService.save(promotion);
    }

    @DeleteMapping("/{promotionId}")
    public void stopPromotion(@PathVariable long promotionId) {
        promotionService.stop(promotionId);
    }
}
