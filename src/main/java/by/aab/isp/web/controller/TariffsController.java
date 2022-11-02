package by.aab.isp.web.controller;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.SCHEMA_REDIRECT;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

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

import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.dto.tariff.TariffDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.TariffService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tariffs")
@RequiredArgsConstructor
public class TariffsController {

    private static final Sort ORDER_BY_NAME = Sort.by("name");

    private final TariffService tariffService;

    @GetMapping
    public String viewAll(@RequestParam(name = "page", defaultValue = "1") int pageNumber, @RequestAttribute(required = false) EmployeeDto activeEmployee, Model model) {
        pageNumber = Integer.max(pageNumber - 1, 0);
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, ORDER_BY_NAME);
        Page<ShowTariffDto> tariffs = activeEmployee != null ? tariffService.getAll(request)
                                                             : tariffService.getActive(request);
        model.addAttribute("page", tariffs);
        return "manage-tariffs";
    }

    @GetMapping("/new")
    public String createNewTariff(@RequestAttribute EmployeeDto activeEmployee, @RequestParam(required = false) String redirect, Model model) {
        if (null == redirect || redirect.isBlank()) {
            redirect = "/tariffs";
        }
        model.addAttribute("redirect", redirect);
        model.addAttribute("tariff", new TariffDto());
        return "edit-tariff";
    }

    @GetMapping("/{tariffId}")
    public String editTariff(@RequestAttribute EmployeeDto activeEmployee, @PathVariable long tariffId, @RequestParam(required = false) String redirect, Model model) {
        if (null == redirect || redirect.isBlank()) {
            redirect = "/tariffs";
        }
        model.addAttribute("tariff", tariffService.getById(tariffId));
        model.addAttribute("redirect", redirect);
        return "edit-tariff";
    }

    @PostMapping
    public String saveTariff(@RequestAttribute EmployeeDto activeEmployee,
            @RequestParam(required = false) Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) Integer bandwidth,
            @RequestParam(name = "included-traffic") Double includedTraffic,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) String active,
            @RequestParam String redirect) {
        TariffDto tariff = new TariffDto();
        tariff.setId(id);
        tariff.setName(name);
        tariff.setDescription(description);
        tariff.setBandwidth(bandwidth != 0 ? bandwidth
                                           : null);
        tariff.setIncludedTraffic(includedTraffic != 0 ? (long) (includedTraffic * 1024 * 1024)
                                                       : null);
        tariff.setPrice(price);
        tariff.setActive(active != null);
        tariffService.save(tariff);
        return SCHEMA_REDIRECT + redirect;
    }

    @PostMapping("/generate")
    public String generateTariffs(@RequestAttribute EmployeeDto activeEmployee, @RequestParam int quantity, @RequestParam(required = false) String active,
            @RequestParam String redirect) {
        tariffService.generateTariffs(quantity, active != null);
        return SCHEMA_REDIRECT + redirect;
    }

    @ExceptionHandler
    public String handleNonEmployee(ServletRequestBindingException e,
            @RequestAttribute(required = false) EmployeeDto activeEmployee,
            @RequestAttribute(required = false) UserDto activeUser,
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
