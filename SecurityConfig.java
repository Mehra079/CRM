package com.project.Bootcamp.config;

import com.project.Bootcamp.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private int jwtExpirationMs;


    private final JwtTokenUtil jwtTokenUtil;

    public SecurityConfig(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }


    // Define the Password Encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Define the main security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection for stateless APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/users/register",
                                "/api/v1/users/login",
                                "/api/v1/users/verify",
                                "/api/v1/users/forgot-password",
                                "/api/v1/users/verify-otp"
                        ).permitAll()  // Allow public access to these endpoints
                        .anyRequest().authenticated()  // Authenticate all other requests
                )
                .sessionManagement(sessionManagement -> sessionManagement.disable());  // Use JWT, so disable session management

        return http.build();
    }
    // Lazily initialize the AuthenticationManager Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
