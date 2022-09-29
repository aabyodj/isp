package by.aab.isp;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import by.aab.isp.service.PromotionService;
import by.aab.isp.service.SubscriptionService;
import by.aab.isp.service.TariffService;
import by.aab.isp.service.UserService;
import by.aab.isp.web.command.customer.EditCustomerCommand;
import by.aab.isp.web.command.employee.EditEmployeeCommand;
import by.aab.isp.web.command.promotion.EditPromotionCommand;
import by.aab.isp.web.command.tariff.EditTariffCommand;

@Configuration
@ComponentScan
public class ContextConfiguration {
	
	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Bean({"edit_customer", "new_customer"})
	public EditCustomerCommand editCustomerCommand(UserService userService, SubscriptionService subscriptionService, TariffService tariffService) {
		return new EditCustomerCommand(userService, subscriptionService, tariffService);
	}
	
	@Bean({"edit_employee", "new_employee"})
	public EditEmployeeCommand editEmployeeCommand(UserService userService) {
		return new EditEmployeeCommand(userService);
	}
	
	@Bean({"edit_tariff", "new_tariff", "view_tariff"})
	public EditTariffCommand editTariffCommand(TariffService tariffService) {
		return new EditTariffCommand(tariffService);
	}
	
	@Bean({"edit_promotion", "new_promotion"})
	public EditPromotionCommand editPromotionCommand(PromotionService promotionService) {
		return new EditPromotionCommand(promotionService);
	}
}
