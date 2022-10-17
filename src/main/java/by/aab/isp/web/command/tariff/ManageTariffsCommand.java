package by.aab.isp.web.command.tariff;

import by.aab.isp.dto.tariff.ShowTariffDto;
import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.Pagination;
import by.aab.isp.service.TariffService;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ManageTariffsCommand extends by.aab.isp.web.command.Command {

    private final TariffService tariffService;

    @Override
    public String execute(HttpServletRequest req) {
        Pagination pagination = new Pagination();
        pagination.setPageSize(DEFAULT_PAGE_SIZE);
        String page = req.getParameter("page");
        pagination.setPageNumber(page != null ? Integer.parseInt(page)
                                              : 0);
        List<ShowTariffDto> tariffs = tariffService.getAll(pagination);
        if (!tariffs.isEmpty()) {
            req.setAttribute("tariffs", tariffs);
        }
        req.setAttribute("pagination", pagination);
        return "jsp/manage-tariffs.jsp";
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof EmployeeDto;
    }
}
