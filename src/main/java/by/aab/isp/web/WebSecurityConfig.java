package by.aab.isp.web;

import static by.aab.isp.service.security.AppUserDetails.ROLE_ADMIN;
import static by.aab.isp.service.security.AppUserDetails.ROLE_MANAGER;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(requests -> requests
                        .mvcMatchers(HttpMethod.GET, "/", "/promotions", "/tariffs", "/login*",
                                "/api", "/promotions", "/tariffs",
                                "/css/**", "/js**", "/images/**", "/error/**").permitAll()
                        .mvcMatchers("/customers/**", "/promotions/**", "/tariffs/**",
                                "/api/customers/**", "/api/promotions/**", "/api/promotions/**").hasAnyAuthority(ROLE_ADMIN, ROLE_MANAGER) //FIXME must be .hasAnyRole()
                        .mvcMatchers("/employees/**", "/api/employees/**").hasAuthority(ROLE_ADMIN)  //FIXME must be .hasRole()
                        .mvcMatchers("/my_account/**").authenticated()
                        .anyRequest().denyAll())
                .httpBasic(basic -> {})
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(false)
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
