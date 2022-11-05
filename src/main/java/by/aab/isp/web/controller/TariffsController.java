package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import by.aab.isp.dto.tariff.TariffEditDto;
import by.aab.isp.dto.tariff.TariffViewDto;
import by.aab.isp.dto.user.EmployeeViewDto;
import by.aab.isp.dto.user.UserViewDto;
import by.aab.isp.service.TariffService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tariffs")
@RequiredArgsConstructor
public class TariffsController {

    private static final Sort ORDER_BY_NAME = Sort.by("name");

    private final TariffService tariffService;

    @GetMapping
    public String viewAll(@RequestParam(name = "page", defaultValue = "1") int pageNumber,
            @RequestAttribute(required = false) EmployeeViewDto activeEmployee, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, ORDER_BY_NAME);
        Page<TariffViewDto> tariffs = activeEmployee != null ? tariffService.getAll(request)
                                                             : tariffService.getActive(request);
        model.addAttribute("page", tariffs);
        return "manage-tariffs";
    }

    @ModelAttribute("tariff")
    public TariffEditDto initTariff() {
        return new TariffEditDto();
    }

    @ModelAttribute("redirect")
    public String getDefaultRedirect() {
        return "/tariffs";
    }

    @GetMapping("/new")
    public String createNewTariff(@RequestAttribute EmployeeViewDto activeEmployee) {
        return "edit-tariff";
    }

    @GetMapping("/{tariffId}")
    public String editTariff(@RequestAttribute EmployeeViewDto activeEmployee,
            @PathVariable long tariffId, Model model) {
        model.addAttribute("tariff", tariffService.getById(tariffId));
        return "edit-tariff";
    }

    @PostMapping({"/new", "/{tariffId}"})
    public String saveTariff(@RequestAttribute EmployeeViewDto activeEmployee,
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
    public String generateTariffs(@RequestAttribute EmployeeViewDto activeEmployee,
            @RequestParam int quantity, @RequestParam(required = false) String active,
            @ModelAttribute("redirect") String redirect) {
        tariffService.generateTariffs(quantity, active != null);
        return SCHEMA_REDIRECT + redirect;
    }

    @ExceptionHandler
    public String handleNonEmployee(ServletRequestBindingException e,
            @RequestAttribute(required = false) EmployeeViewDto activeEmployee,
            @RequestAttribute(required = false) UserViewDto activeUser,
            HttpServletRequest req)
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
