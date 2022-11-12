package by.aab.isp.web.controller.mvc;

import static by.aab.isp.service.security.AppUserDetails.ROLE_ADMIN;
import static by.aab.isp.service.security.AppUserDetails.ROLE_MANAGER;
import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.DEFAULT_TARIFFS_SORT;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.util.Objects;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import by.aab.isp.service.TariffService;
import by.aab.isp.service.dto.tariff.TariffEditDto;
import by.aab.isp.service.dto.tariff.TariffViewDto;
import by.aab.isp.service.dto.user.EmployeeViewDto;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tariffs")
@RolesAllowed({ROLE_ADMIN, ROLE_MANAGER})
@RequiredArgsConstructor
public class TariffsController {

    private final TariffService tariffService;

    @GetMapping
    @PermitAll
    public String viewAll(@RequestParam(name = "page", defaultValue = "1") int pageNumber,
            @RequestAttribute(required = false) EmployeeViewDto activeEmployee, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, DEFAULT_TARIFFS_SORT);
        Page<TariffViewDto> tariffs = activeEmployee != null ? tariffService.getAll(request)
                                                             : tariffService.getActive(request);
        model.addAttribute("page", tariffs);
        return "manage-tariffs";
    }

    @ModelAttribute("redirect")
    @PermitAll
    public String getDefaultRedirect() {    //TODO get rid of this
        return "/tariffs";
    }

    @GetMapping("/new")
    public String createNewTariff(Model model) {
        model.addAttribute("tariff", new TariffEditDto());
        return "edit-tariff";
    }

    @GetMapping("/{tariffId}")
    public String editTariff(@PathVariable long tariffId, Model model) {
        model.addAttribute("tariff", tariffService.getById(tariffId));
        return "edit-tariff";
    }

    @PostMapping({"/new", "/{tariffId}"})
    public String saveTariff(
            @PathVariable(required = false) Long tariffId,
            @Valid @ModelAttribute("tariff") TariffEditDto tariff,
            BindingResult bindingResult,
            @ModelAttribute("redirect") String redirect) {
        if (!Objects.equals(tariffId, tariff.getId())) {
            throw new IllegalArgumentException();
        }
        if (bindingResult.hasErrors()) {
            return "edit-tariff";
        }
        tariffService.save(tariff);
        return SCHEMA_REDIRECT + redirect;
    }

    @PostMapping("/generate")
    public String generateTariffs(
            @RequestParam int quantity, @RequestParam(required = false) String active,
            @ModelAttribute("redirect") String redirect) {
        tariffService.generateTariffs(quantity, active != null);
        return SCHEMA_REDIRECT + redirect;
    }
}
