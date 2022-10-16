package by.aab.isp.web.command.tariff;

import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.entity.Tariff;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class EditTariffCommand extends Command {

    private final TariffService tariffService;

    public EditTariffCommand(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        String id = req.getParameter("id");
        Tariff tariff = tariffService.getById(id != null ? Long.parseLong(id)
                                                         : null);
        if (tariff.getBandwidth() == null) {
            tariff.setBandwidth(0);
        }
        req.setAttribute("tariff", tariff);
        req.setAttribute("redirect", "?action=manage_tariffs"); //TODO: determine a referer
        return "jsp/edit-tariff.jsp";
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof EmployeeDto;
    }

}
