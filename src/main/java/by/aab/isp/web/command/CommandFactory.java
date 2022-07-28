package by.aab.isp.web.command;

import by.aab.isp.service.ServiceFactory;
import by.aab.isp.service.TariffService;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    private final Map<String, Command> commands = new HashMap<>();
    private final Command index = request -> "jsp/index.jsp";
    private final Command error = request -> "jsp/error.jsp";
    
    private CommandFactory() {
        commands.put("tariffs", new ShowAllTariffsCommand(TariffService.getInstance()));
        commands.put("show_tariff", new ShowTariffCommand(TariffService.getInstance()));
    }
    
    public Command getCommand(String commandName) {
        if (null == commandName) return index;
        Command result = commands.get(commandName);
        return result != null ? result : error;
    }
    
    private static class BillPughSingleton {
        static final CommandFactory INSTANCE = new CommandFactory();
    }
    
    public static CommandFactory getInstance() {
        return BillPughSingleton.INSTANCE;
    }
    
    public void destroy() {
        ServiceFactory.getInstance().destroy();
    }

}
