package com.mb.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig  
{
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                             .requestMatchers("/login", "/signup", "/css/**", "/images/**", "/js/**")
                      .permitAll() // Allow public access to these pages and static resources
                          .anyRequest()
                    .authenticated()  // All other requests must be authenticated // Allow all requests without authentication
            ).formLogin(login -> login
                    .loginPage("/login")  // Use the custom login page
                    .loginProcessingUrl("/login")  // The URL Spring Security will handle for form submissions
                    .usernameParameter("username")  // The name of the username input field in your form
                    .passwordParameter("password")  // The name of the password input field in your form
                    .defaultSuccessUrl("/home", true)  // Redirect to the home page on successful login
                    .failureUrl("/login?error=true"))
            // Configure OAuth2 login
            .oauth2Login(oauth -> oauth
                .loginPage("/login") // Same custom login page
                .defaultSuccessUrl("/home", true) // Redirect on successful OAuth2 login
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")  // The logout URL
                    .logoutSuccessUrl("/login?logout=true")  // Redirect to login page after logout
                    .permitAll())
            .csrf(csrf -> csrf.disable());  // Disables CSRF protection (use with caution)
        return http.build();
    }
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
}