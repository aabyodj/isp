package by.aab.isp.web.command;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import by.aab.isp.web.command.account.CheckLoginCommand;
import by.aab.isp.web.command.account.LoginCommand;
import by.aab.isp.web.command.account.LogoutCommand;
import by.aab.isp.web.command.account.MyAccountCommand;
import by.aab.isp.web.command.account.UpdateMyCredentialsCommand;
import by.aab.isp.web.command.customer.EditCustomerCommand;
import by.aab.isp.web.command.customer.GenerateCustomersCommand;
import by.aab.isp.web.command.customer.ManageCustomersCommand;
import by.aab.isp.web.command.customer.ReplenishBalanceCommand;
import by.aab.isp.web.command.customer.SaveCustomerCommand;
import by.aab.isp.web.command.employee.EditEmployeeCommand;
import by.aab.isp.web.command.employee.GenerateEmployeesCommand;
import by.aab.isp.web.command.employee.ManageEmployeesCommand;
import by.aab.isp.web.command.employee.SaveEmployeeCommand;
import by.aab.isp.web.command.promotion.EditPromotionCommand;
import by.aab.isp.web.command.promotion.GeneratePromotionsCommand;
import by.aab.isp.web.command.promotion.ManagePromotionsCommand;
import by.aab.isp.web.command.promotion.SavePromotionCommand;
import by.aab.isp.web.command.promotion.StopPromotionCommand;
import by.aab.isp.web.command.subscription.CancelSubscriptionCommand;
import by.aab.isp.web.command.subscription.SubscribeCommand;
import by.aab.isp.web.command.tariff.EditTariffCommand;
import by.aab.isp.web.command.tariff.GenerateTariffsCommand;
import by.aab.isp.web.command.tariff.ManageTariffsCommand;
import by.aab.isp.web.command.tariff.SaveTariffCommand;

@Component
public class CommandDispatcher {

    private final ApplicationContext context;
    private final Map<String, Class<? extends Command>> commands = new HashMap<>();
    private final Class<? extends Command> index;

    public CommandDispatcher(ApplicationContext context) {
        this.context = context;
        index = HomeCommand.class;
        //TODO: generate this automatically from the context
        commands.put("login", LoginCommand.class);
        commands.put("check_login", CheckLoginCommand.class);
        commands.put("logout", LogoutCommand.class);
        commands.put("my_account", MyAccountCommand.class);
        commands.put("replenish_balance", ReplenishBalanceCommand.class);
        commands.put("subscribe", SubscribeCommand.class);
        commands.put("cancel_subscription", CancelSubscriptionCommand.class);
        commands.put("update_my_credentials", UpdateMyCredentialsCommand.class);
        commands.put("manage_promotions", ManagePromotionsCommand.class);
        commands.put("edit_promotion", EditPromotionCommand.class);
        commands.put("new_promotion", getCommandClass("edit_promotion"));
        commands.put("generate_promotions", GeneratePromotionsCommand.class);
        commands.put("save_promotion", SavePromotionCommand.class);
        commands.put("stop_promotion", StopPromotionCommand.class);
        commands.put("manage_tariffs", ManageTariffsCommand.class);
        commands.put("edit_tariff", EditTariffCommand.class);
        commands.put("new_tariff", getCommandClass("edit_tariff"));
        commands.put("generate_tariffs", GenerateTariffsCommand.class);
        commands.put("view_tariff", getCommandClass("edit_tariff"));
        commands.put("save_tariff", SaveTariffCommand.class);
        commands.put("manage_customers", ManageCustomersCommand.class);
        commands.put("edit_customer", EditCustomerCommand.class);
        commands.put("new_customer", getCommandClass("edit_customer"));
        commands.put("generate_customers", GenerateCustomersCommand.class);
        commands.put("save_customer", SaveCustomerCommand.class);
        commands.put("manage_employees", ManageEmployeesCommand.class);
        commands.put("edit_employee", EditEmployeeCommand.class);
        commands.put("new_employee", getCommandClass("edit_employee"));
        commands.put("generate_employees", GenerateEmployeesCommand.class);
        commands.put("save_employee", SaveEmployeeCommand.class);
    }
    
    public Class<? extends Command> getCommandClass(String commandName) {
        if (null == commandName || commandName.isBlank()) {
            return index;
        }
        Class<? extends Command> result = commands.get(commandName);
        if (null == result) {
            throw new NoSuchElementException("Command '" + commandName + "' not found");
        }
        return result;
    }
    
    public Command getCommand(String commandName) {
        return context.getBean(getCommandClass(commandName));
    }
}
