package by.aab.isp.web.command.tariff;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Tariff;
import by.aab.isp.entity.User;
import by.aab.isp.service.Pagination;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.FormatUtil;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import static by.aab.isp.web.Const.DEFAULT_PAGE_SIZE;

@Component("manage_tariffs")
public class ManageTariffsCommand extends by.aab.isp.web.command.Command {

    private final TariffService tariffService;
    private final FormatUtil util;

    public ManageTariffsCommand(TariffService tariffService, FormatUtil util) {
        this.tariffService = tariffService;
		this.util = util;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Pagination pagination = new Pagination();
        pagination.setPageSize(DEFAULT_PAGE_SIZE);
        String page = req.getParameter("page");
        pagination.setPageNumber(page != null ? Integer.parseInt(page)
                                              : 0);
        Iterable<Tariff> tariffs = tariffService.getAll(pagination);
        if (tariffs.spliterator().estimateSize() > 0) {
            req.setAttribute("tariffs", tariffs);
            req.setAttribute("util", util);
        }
        req.setAttribute("pagination", pagination);
        return "jsp/manage-tariffs.jsp";
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
