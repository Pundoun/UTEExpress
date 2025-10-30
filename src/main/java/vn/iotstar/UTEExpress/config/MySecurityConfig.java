package vn.iotstar.UTEExpress.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import vn.iotstar.UTEExpress.service.impl.AccountDetailService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MySecurityConfig {
	
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
	
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF (chỉ để demo, không khuyến nghị production)
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                		.requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                		.requestMatchers("/customer/**").hasAnyAuthority("CUSTOMER") // Chỉ CUSTOMER truy cập được
                		.requestMatchers("/manager/**").hasAuthority("MANAGER")
                		.requestMatchers("/shippercustocus/**").hasAuthority("SHIPPER_DIRECT")
                		.requestMatchers("/shippercustopost/**").hasAuthority("SHIPPER_CUSTOMER")
                		.requestMatchers("/shipperposttocus/**").hasAuthority("SHIPPER_POST")
                		.requestMatchers("/shipperposttopost/**").hasAuthority("SHIPPER_TRANSFER")
                        .requestMatchers("/","/register", "/login/**", "/register/**",
                        					"/logout/**","/track-order/**",
                        					"/verify-otp","/process-register","/process-reset-password","/forgot-password","/process-forgotpassword",
                        					"/forgot-verify-otp","/process-reset-password","/reset-password").permitAll()
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/scss/**", "/home/**","/manager/**","/shipper/**" ).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/login/waiting", true).permitAll())
                .build();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new AccountDetailService();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}
