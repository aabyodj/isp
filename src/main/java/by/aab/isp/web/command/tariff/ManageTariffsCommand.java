package by.aab.isp.web.command.tariff;

import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.TariffService;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;

@Component
@RequiredArgsConstructor
public class ManageTariffsCommand extends by.aab.isp.web.command.Command {

    private static final Sort ORDER_BY_NAME = Sort.by("name");

    private final TariffService tariffService;

    @Override
    public String execute(HttpServletRequest req) {
        String pageStr = req.getParameter("page");
        int pageNumber = pageStr != null ? Integer.max(Integer.parseInt(pageStr) - 1, 0)
                                         : 0;
        PageRequest request = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, ORDER_BY_NAME);
        Page<ShowTariffDto> tariffs = tariffService.getAll(request);
        req.setAttribute("page", tariffs);
        return "jsp/manage-tariffs.jsp";
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof EmployeeDto;
    }
}
