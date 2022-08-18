package by.aab.isp.web.command.tariff;

import by.aab.isp.entity.Employee;
import by.aab.isp.entity.Tariff;
import by.aab.isp.entity.User;
import by.aab.isp.service.TariffService;
import by.aab.isp.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SaveTariffCommand extends Command {

    private final TariffService tariffService;
    public SaveTariffCommand(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        Tariff tariff = new Tariff();
        tariff.setId(Long.parseLong(req.getParameter("id")));
        tariff.setName(req.getParameter("name"));
        tariff.setDescription(req.getParameter("description"));
        int bandwidth = Integer.parseInt(req.getParameter("bandwidth"));
        tariff.setBandwidth(bandwidth != 0 ? bandwidth
                                           : null);
        double traffic = Double.parseDouble(req.getParameter("included-traffic")) * 1024 * 1024;
        tariff.setIncludedTraffic(traffic != 0 ? (long) traffic
                                               : null);
        tariff.setPrice(new BigDecimal(req.getParameter("price")));
        tariff.setActive(req.getParameter("active") != null);
        tariffService.save(tariff);
        String redirect = req.getParameter("redirect");
        return SCHEMA_REDIRECT + req.getContextPath() + redirect;
    }

    @Override
    public boolean isAllowedForUser(User user) {
        return user instanceof Employee;
    }
}
