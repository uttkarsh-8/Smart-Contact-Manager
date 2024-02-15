package com.smart.smartContactManager.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class MyConfig {

    @Bean
    public UserDetailsService getUserDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF if your application does not require it
                .csrf(AbstractHttpConfigurer::disable)
                // Authorize HTTP requests
                .authorizeHttpRequests(authz -> authz
                        // Require 'ROLE_ADMIN' for paths under '/admin/'
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Require 'ROLE_USER' for paths under '/user/'
                        .requestMatchers("/user/**").hasRole("USER")
                        // All other paths must be authenticated
                        .anyRequest().permitAll()
                )
                // Configure form-based login
                .formLogin(formLogin -> formLogin
                        .loginPage("/signin") // Specify your custom login page
                        .loginProcessingUrl("/signin") // Specify the URL to submit the username and password
                        .defaultSuccessUrl("/", true) // Specify the default URL to redirect after successful login
                        .failureUrl("/signin?error=true") // Specify the URL to redirect after failed login
                        .permitAll() // Allow all users to view the login page without being logged in
                );

        return http.build();
    }
}

