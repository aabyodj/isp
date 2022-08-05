package by.aab.isp.web.command;

import by.aab.isp.service.ServiceFactory;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    private final Map<String, Command> commands = new HashMap<>();
    private final Command index;
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    
    private CommandFactory() {
        TariffService tariffService = serviceFactory.getService(TariffService.class);
        UserService userService = serviceFactory.getService(UserService.class);
        index = new HomeCommand(tariffService);
        commands.put("edit_tariff", new EditTariffCommand(tariffService));
        commands.put("new_tariff", getCommand("edit_tariff"));
        commands.put("view_tariff", getCommand("edit_tariff"));
        commands.put("save_tariff", new SaveTariffCommand(tariffService));
        commands.put("manage_users", new ManageUsersCommand(userService));
        commands.put("edit_user", new EditUserCommand(userService));
        commands.put("new_user", getCommand("edit_user"));
        commands.put("save_user", new SaveUserCommand(userService));
    }
    
    public Command getCommand(String commandName) {
        if (null == commandName || commandName.isBlank()) return index;
        Command result = commands.get(commandName);
        if (null == result) throw new RuntimeException("Command '" + commandName + "' not found");
        return result;
    }

    private static class BillPughSingleton {
        static final CommandFactory INSTANCE = new CommandFactory();
    }
    
    public static CommandFactory getInstance() {
        return BillPughSingleton.INSTANCE;
    }

    public void init() {
        serviceFactory.init();
    }
    
    public void destroy() {
        serviceFactory.destroy();
    }

}
