package com.is4tech.invoicemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/javainuse-openapi/**").permitAll()
                .requestMatchers("/invoice-management/v0.1/customer/**").hasAnyAuthority("ROLE_CUSTOMER")
                .requestMatchers("/invoice-management/v0.1/detail-invoice-products/**").hasAnyAuthority("ROLE_DETAIL_INVOICE_PRODUCTS")
                .requestMatchers("/invoice-management/v0.1/payment-method/**").hasAnyAuthority("ROLE_PAYMENT_METHOD")
                .requestMatchers("/invoice-management/v0.1/product/**").hasAnyAuthority("ROLE_PRODUCT")
                .requestMatchers("/invoice-management/v0.1/report/**").hasAnyAuthority("ROLE_REPORT")
                .requestMatchers("/invoice-management/v0.1/status-invoice/**").hasAnyAuthority("ROLE_STATUS_INVOICE")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
