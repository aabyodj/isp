package by.aab.isp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import by.aab.isp.web.interceptor.UserSessionInterceptor;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class ContextConfiguration implements WebMvcConfigurer {

    private final UserSessionInterceptor userSessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(userSessionInterceptor)
	            .addPathPatterns("/**");
	}

	public static void main(String[] args) throws Exception {
        SpringApplication.run(ContextConfiguration.class, args);
    }

}
