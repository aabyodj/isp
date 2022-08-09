package by.aab.isp.web.command;

import by.aab.isp.service.*;
import by.aab.isp.web.util.UserUtil;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    private final Map<String, Command> commands = new HashMap<>();
    private final Command index;
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private final UserUtil userUtil;
    
    private CommandFactory() {
        PromotionService promotionService = serviceFactory.getService(PromotionService.class);
        TariffService tariffService = serviceFactory.getService(TariffService.class);
        CustomerAccountService customerService = serviceFactory.getService(CustomerAccountService.class);
        UserService userService = serviceFactory.getService(UserService.class);
        userUtil = new UserUtil(customerService, userService);
        index = new HomeCommand(promotionService, tariffService);
        commands.put("login", (req) -> "jsp/login-form.jsp");
        commands.put("check_login", new CheckLoginCommand(userService));
        commands.put("logout", new LogoutCommand());
        commands.put("edit_promotion", new EditPromotionCommand(promotionService));
        commands.put("new_promotion", getCommand("edit_promotion"));
        commands.put("save_promotion", new SavePromotionCommand(promotionService));
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

    //TODO: use a separate factory for this
    public UserUtil getUserUtil() {
        return userUtil;
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
