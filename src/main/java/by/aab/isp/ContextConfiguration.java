package by.aab.isp;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import by.aab.isp.config.HikariConfig;

@Configuration
@ComponentScan
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableJpaRepositories
public class ContextConfiguration {

	@Bean
	public EntityManagerFactory entityManagerFactory(HikariConfig config) {
	    return Persistence.createEntityManagerFactory("hibernate", config);
	}

	@Bean
	public TransactionManager transactionManager(EntityManagerFactory factory) {
	    return new JpaTransactionManager(factory);
	}
}
