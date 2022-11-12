package by.aab.isp.web.controller.rest;

import static by.aab.isp.service.security.AppUserDetails.ROLE_ADMIN;
import static by.aab.isp.service.security.AppUserDetails.ROLE_MANAGER;
import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;
import static by.aab.isp.web.Const.DEFAULT_TARIFFS_SORT;

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

import by.aab.isp.service.TariffService;
import by.aab.isp.service.dto.tariff.TariffEditDto;
import by.aab.isp.service.dto.tariff.TariffViewDto;
import by.aab.isp.service.dto.user.EmployeeViewDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tariffs")
@RolesAllowed({ROLE_ADMIN, ROLE_MANAGER})
@RequiredArgsConstructor
public class TariffRestController {

    private static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(0, DEFAULT_PAGE_SIZE, DEFAULT_TARIFFS_SORT);

    private final TariffService tariffService;

    @GetMapping
    @PermitAll
    public Page<TariffViewDto> getAll(@RequestBody(required = false) PageRequest request,
            @RequestAttribute(required = false) EmployeeViewDto activeEmployee) {
        if (null == request) {
            request = DEFAULT_PAGE_REQUEST;
        }
        return activeEmployee != null ? tariffService.getAll(request)
                                      : tariffService.getActive(request);
    }

    @GetMapping("/{tariffId}")
    public TariffEditDto getTariff(@PathVariable long tariffId, @RequestAttribute EmployeeViewDto activeEmployee) {
        return tariffService.getById(tariffId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TariffEditDto createTariff(@Valid @RequestBody TariffEditDto tariff, @RequestAttribute EmployeeViewDto activeEmployee) {
        tariff.setId(null);
        return tariffService.save(tariff);
    }

    @PutMapping("/{tariffId}")
    public TariffEditDto updateTariff(@PathVariable long tariffId, @Valid @RequestBody TariffEditDto tariff, 
            @RequestAttribute EmployeeViewDto activeEmployee) {
        tariff.setId(tariffId);
        return tariffService.save(tariff);
    }

    @DeleteMapping("/{tariffId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateTariff(@PathVariable long tariffId, @RequestAttribute EmployeeViewDto activeEmployee) {
        tariffService.deactivate(tariffId);
    }
}
