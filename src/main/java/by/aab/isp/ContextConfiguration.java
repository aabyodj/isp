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
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import by.aab.isp.config.HikariConfig;
import by.aab.isp.web.interceptor.UserSessionInterceptor;

@Configuration
@ComponentScan
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableJpaRepositories
public class ContextConfiguration extends WebMvcConfigurationSupport {

	@Bean
	public EntityManagerFactory entityManagerFactory(HikariConfig config) {
	    return Persistence.createEntityManagerFactory("hibernate", config);
	}

	@Bean
	public TransactionManager transactionManager(EntityManagerFactory factory) {
	    return new JpaTransactionManager(factory);
	}

	@Bean
	public InternalResourceViewResolver jspViewResolver() {
	    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
	    resolver.setPrefix("/WEB-INF/jsp/");
	    resolver.setSuffix(".jsp");
	    resolver.setViewClass(JstlView.class);
	    return resolver;
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("css/**", "images/**", "js/**")
	            .addResourceLocations("classpath:/static/css/", "classpath:/static/images/", "classpath:/static/js/");
	}

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(getApplicationContext().getBean(UserSessionInterceptor.class))
	            .addPathPatterns("/**");
	}

}
