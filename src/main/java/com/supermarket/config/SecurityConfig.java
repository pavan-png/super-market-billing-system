package com.supermarket.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     http
         .authorizeHttpRequests(auth -> auth
             .requestMatchers("/login-page", "/css/**", "/js/**").permitAll()
             .requestMatchers("/").permitAll()
             .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("MANAGER")
             .requestMatchers(HttpMethod.POST, "/api/orders").hasRole("CASHIER")
             .requestMatchers("/billing", "/billing**").hasRole("CASHIER")
             .requestMatchers("/upi-payment").hasRole("CASHIER")  // New: Secure UPI page
             .requestMatchers("/manager", "/manager**").hasRole("MANAGER")
             .anyRequest().authenticated()
         )
         .formLogin(form -> form
             .loginPage("/login-page")
             .loginProcessingUrl("/login-page")
             .defaultSuccessUrl("/", true)
             .permitAll()
         )
         .logout(logout -> logout
             .logoutSuccessUrl("/login-page")
             .permitAll()
         )
         .csrf(csrf -> csrf.disable()); // Temporarily disable for easier testing (remove in production after Thymeleaf CSRF is working)
     return http.build();
 }

 @Bean
 public UserDetailsService userDetailsService() {
     UserDetails cashier = User.withDefaultPasswordEncoder()
             .username("cashier")
             .password("password")
             .roles("CASHIER")
             .build();

     UserDetails manager = User.withDefaultPasswordEncoder()
             .username("manager")
             .password("manager123")
             .roles("MANAGER")
             .build();

     return new InMemoryUserDetailsManager(cashier, manager);
 }
}