package com.example.restfull_web_services.configs;

import com.example.restfull_web_services.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig — HTTP Basic authentication backed by the JPA User model.
 *
 * Username = email address. Password must be BCrypt-encoded in the DB.
 *
 * Access matrix:
 * ┌───────────────────────────────────┬────────────────────────┐
 * │ Path pattern │ Access │
 * ├───────────────────────────────────┼────────────────────────┤
 * │ /swagger-ui/** /api-docs/** │ Permit all (Swagger) │
 * │ /swagger-ui.html │ Permit all (Swagger) │
 * │ /explorer/** │ Permit all (HAL) │
 * │ /h2-console/** │ Permit all (dev tool) │
 * │ /actuator/health /actuator/info │ Permit all │
 * │ Everything else │ Authenticated (Basic) │
 * └───────────────────────────────────┴────────────────────────┘
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Wires our UserDetailsService + PasswordEncoder into Spring's auth pipeline.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Spring Security 7: UserDetailsService is passed via constructor (setter
        // removed)
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())

                // Stateless — every request must carry credentials
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Disable CSRF for stateless REST; allow H2 console (frame-based UI)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .disable())

                // Allow H2 console to render inside an iframe
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                .authorizeHttpRequests(auth -> auth
                        // Swagger UI
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api-docs",
                                "/api-docs/**")
                        .permitAll()
                        // HAL Explorer
                        .requestMatchers("/explorer/**").permitAll()
                        // H2 console
                        .requestMatchers("/h2-console/**").permitAll()
                        // Actuator probes
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        // Everything else requires a valid DB user
                        .anyRequest().authenticated())

                .httpBasic(basic -> {
                });

        return http.build();
    }
}
