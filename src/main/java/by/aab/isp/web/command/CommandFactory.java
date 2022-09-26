package by.aab.isp.web.command;

import by.aab.isp.service.*;
import by.aab.isp.web.command.account.*;
import by.aab.isp.web.command.customer.*;
import by.aab.isp.web.command.employee.EditEmployeeCommand;
import by.aab.isp.web.command.employee.GenerateEmployeesCommand;
import by.aab.isp.web.command.employee.ManageEmployeesCommand;
import by.aab.isp.web.command.employee.SaveEmployeeCommand;
import by.aab.isp.web.command.promotion.*;
import by.aab.isp.web.command.subscription.CancelSubscriptionCommand;
import by.aab.isp.web.command.subscription.SubscribeCommand;
import by.aab.isp.web.command.tariff.EditTariffCommand;
import by.aab.isp.web.command.tariff.GenerateTariffsCommand;
import by.aab.isp.web.command.tariff.ManageTariffsCommand;
import by.aab.isp.web.command.tariff.SaveTariffCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.context.ApplicationContext;

public class CommandFactory {

    private final Map<String, Command> commands = new HashMap<>();
    private Command index;

    private CommandFactory() {
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

    public void init(ApplicationContext context) {
        index = new HomeCommand(
                context.getBean(PromotionService.class), 
                context.getBean(TariffService.class));
        commands.put("login", new LoginCommand());
        commands.put("check_login", new CheckLoginCommand(
        		context.getBean(UserService.class)));
        commands.put("logout", new LogoutCommand());
        commands.put("my_account", new MyAccountCommand(
        		context.getBean(SubscriptionService.class),
        		context.getBean(TariffService.class)));
        commands.put("replenish_balance", new ReplenishBalanceCommand(
        		context.getBean(UserService.class)));
        commands.put("subscribe", new SubscribeCommand(
        		context.getBean(SubscriptionService.class)));
        commands.put("cancel_subscription", new CancelSubscriptionCommand(
        		context.getBean(SubscriptionService.class)));
        commands.put("update_my_credentials", new UpdateMyCredentialsCommand(
        		context.getBean(UserService.class)));
        commands.put("manage_promotions", new ManagePromotionsCommand(
        		context.getBean(PromotionService.class)));
        commands.put("edit_promotion", new EditPromotionCommand(
        		context.getBean(PromotionService.class)));
        commands.put("new_promotion", getCommand("edit_promotion"));
        commands.put("generate_promotions", new GeneratePromotionsCommand(
        		context.getBean(PromotionService.class)));
        commands.put("save_promotion", new SavePromotionCommand(
        		context.getBean(PromotionService.class)));
        commands.put("stop_promotion", new StopPromotionCommand(
        		context.getBean(PromotionService.class)));
        commands.put("manage_tariffs", new ManageTariffsCommand(
        		context.getBean(TariffService.class)));
        commands.put("edit_tariff", new EditTariffCommand(
        		context.getBean(TariffService.class)));
        commands.put("new_tariff", getCommand("edit_tariff"));
        commands.put("generate_tariffs", new GenerateTariffsCommand(
        		context.getBean(TariffService.class)));
        commands.put("view_tariff", getCommand("edit_tariff"));
        commands.put("save_tariff", new SaveTariffCommand(
        		context.getBean(TariffService.class)));
        commands.put("manage_customers", new ManageCustomersCommand(
        		context.getBean(UserService.class)));
        commands.put("edit_customer", new EditCustomerCommand(
        		context.getBean(UserService.class),
        		context.getBean(SubscriptionService.class),
        		context.getBean(TariffService.class)));
        commands.put("new_customer", getCommand("edit_customer"));
        commands.put("generate_customers", new GenerateCustomersCommand(
        		context.getBean(UserService.class)));
        commands.put("save_customer", new SaveCustomerCommand(
        		context.getBean(UserService.class),
        		context.getBean(SubscriptionService.class)));
        commands.put("manage_employees", new ManageEmployeesCommand(
        		context.getBean(UserService.class)));
        commands.put("edit_employee", new EditEmployeeCommand(
        		context.getBean(UserService.class)));
        commands.put("new_employee", getCommand("edit_employee"));
        commands.put("generate_employees", new GenerateEmployeesCommand(
        		context.getBean(UserService.class)));
        commands.put("save_employee", new SaveEmployeeCommand(
        		context.getBean(UserService.class)));
    }
    
    public void destroy() {
    }

}
