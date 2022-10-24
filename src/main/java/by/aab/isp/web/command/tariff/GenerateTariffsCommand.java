package by.aab.isp.web.command.tariff;

import by.aab.isp.dto.user.EmployeeDto;
import by.aab.isp.dto.user.UserDto;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.command.Command;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

@Component
public class GenerateTariffsCommand extends Command {

    private final TariffService tariffService;

    public GenerateTariffsCommand(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        boolean active = req.getParameter("active") != null;
        tariffService.generateTariffs(quantity, active);
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(UserDto user) {
        return user instanceof EmployeeDto;
    }
}
