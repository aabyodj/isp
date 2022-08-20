package by.aab.isp.web.command;

import by.aab.isp.service.*;
import by.aab.isp.web.command.account.*;
import by.aab.isp.web.command.customer.EditCustomerCommand;
import by.aab.isp.web.command.customer.ManageCustomersCommand;
import by.aab.isp.web.command.customer.ReplenishBalanceCommand;
import by.aab.isp.web.command.customer.SaveCustomerCommand;
import by.aab.isp.web.command.employee.EditEmployeeCommand;
import by.aab.isp.web.command.employee.ManageEmployeesCommand;
import by.aab.isp.web.command.employee.SaveEmployeeCommand;
import by.aab.isp.web.command.promotion.EditPromotionCommand;
import by.aab.isp.web.command.promotion.ManagePromotionsCommand;
import by.aab.isp.web.command.promotion.SavePromotionCommand;
import by.aab.isp.web.command.promotion.StopPromotionCommand;
import by.aab.isp.web.command.subscription.CancelSubscriptionCommand;
import by.aab.isp.web.command.subscription.SubscribeCommand;
import by.aab.isp.web.command.tariff.EditTariffCommand;
import by.aab.isp.web.command.tariff.ManageTariffsCommand;
import by.aab.isp.web.command.tariff.SaveTariffCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class CommandFactory {

    private final Map<String, Command> commands = new HashMap<>();
    private final Command index;
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();

    private CommandFactory() {
        index = new HomeCommand(
                serviceFactory.getService(PromotionService.class), 
                serviceFactory.getService(TariffService.class));
        commands.put("login", new LoginCommand());
        commands.put("check_login", new CheckLoginCommand(
                serviceFactory.getService(UserService.class)));
        commands.put("logout", new LogoutCommand());
        commands.put("my_account", new MyAccountCommand(
                serviceFactory.getService(SubscriptionService.class),
                serviceFactory.getService(TariffService.class)));
        commands.put("replenish_balance", new ReplenishBalanceCommand(
                serviceFactory.getService(UserService.class)));
        commands.put("subscribe", new SubscribeCommand(
                serviceFactory.getService(SubscriptionService.class)));
        commands.put("cancel_subscription", new CancelSubscriptionCommand(
                serviceFactory.getService(SubscriptionService.class)));
        commands.put("update_my_credentials", new UpdateMyCredentialsCommand(
                serviceFactory.getService(UserService.class)));
        commands.put("manage_promotions", new ManagePromotionsCommand(
                serviceFactory.getService(PromotionService.class)));
        commands.put("edit_promotion", new EditPromotionCommand(
                serviceFactory.getService(PromotionService.class)));
        commands.put("new_promotion", getCommand("edit_promotion"));
        commands.put("save_promotion", new SavePromotionCommand(
                serviceFactory.getService(PromotionService.class)));
        commands.put("stop_promotion", new StopPromotionCommand(
                serviceFactory.getService(PromotionService.class)));
        commands.put("manage_tariffs", new ManageTariffsCommand(
                serviceFactory.getService(TariffService.class)));
        commands.put("edit_tariff", new EditTariffCommand(
                serviceFactory.getService(TariffService.class)));
        commands.put("new_tariff", getCommand("edit_tariff"));
        commands.put("view_tariff", getCommand("edit_tariff"));
        commands.put("save_tariff", new SaveTariffCommand(
                serviceFactory.getService(TariffService.class)));
        commands.put("manage_customers", new ManageCustomersCommand(
                serviceFactory.getService(UserService.class)));
        commands.put("edit_customer", new EditCustomerCommand(
                serviceFactory.getService(UserService.class),
                serviceFactory.getService(SubscriptionService.class),
                serviceFactory.getService(TariffService.class)));
        commands.put("new_customer", getCommand("edit_customer"));
        commands.put("save_customer", new SaveCustomerCommand(
                serviceFactory.getService(UserService.class),
                serviceFactory.getService(SubscriptionService.class)));
        commands.put("manage_employees", new ManageEmployeesCommand(
                serviceFactory.getService(UserService.class)));
        commands.put("edit_employee", new EditEmployeeCommand(
                serviceFactory.getService(UserService.class)));
        commands.put("new_employee", getCommand("edit_employee"));
        commands.put("save_employee", new SaveEmployeeCommand(
                serviceFactory.getService(UserService.class)));
    }
    
    public Command getCommand(String commandName) {
        if (null == commandName || commandName.isBlank()) {
            return index;
        }
        Command result = commands.get(commandName);
        if (null == result) {
            throw new NoSuchElementException("Command '" + commandName + "' not found");
        }
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
