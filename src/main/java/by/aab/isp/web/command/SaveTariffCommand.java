package by.aab.isp.web.command;

import by.aab.isp.entity.Tariff;
import by.aab.isp.service.TariffService;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

import static by.aab.isp.web.Controller.SCHEMA_REDIRECT;

public class SaveTariffCommand implements Command {

    private final TariffService tariffService;
    public SaveTariffCommand(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Override
    public String apply(HttpServletRequest req) {
        Tariff tariff = new Tariff();
        tariff.setId(Long.parseLong(req.getParameter("id")));
        tariff.setName(req.getParameter("name"));
        tariff.setDescription(req.getParameter("description"));
        tariff.setPrice(new BigDecimal(req.getParameter("price")));
        tariffService.save(tariff);
        return SCHEMA_REDIRECT + req.getContextPath();
    }
}
