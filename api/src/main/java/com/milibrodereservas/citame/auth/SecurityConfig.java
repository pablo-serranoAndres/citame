package com.milibrodereservas.citame.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    // definicion de la cadena de filtros
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/citame/public/**", "/citame/auth/**").permitAll() // permite sin autenticación
                        .anyRequest().authenticated() // el resto necesita autenticación
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}
