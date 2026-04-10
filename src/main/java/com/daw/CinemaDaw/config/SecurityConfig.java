package com.daw.CinemaDaw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

         http
        // Desactiva CSRF (necessari per H2)
        .csrf(csrf -> csrf.disable())

        // Permet carregar la consola H2 en un iframe
        .headers(headers -> headers
            .frameOptions(frame -> frame.disable())
        )

        // Configuració d'autoritzacions
        .authorizeHttpRequests(auth -> auth

            // Accés públic
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/","/landing", "/login", "/register", "/css/**", "/cookies/**").permitAll()

            // Rutes protegides per rol
            .requestMatchers("/admin/**" , "/movies/**").hasRole("ADMIN")
            .requestMatchers("/client/**", "/session/**").hasAnyRole("CLIENT", "ADMIN")

            // Qualsevol altra petició necessita autenticació
            .anyRequest().authenticated()
        )

        // Configuració del formulari de login
        .formLogin(form -> form
            .loginPage("/login") // pàgina personalitzada de login
            .successHandler(new CustomLoginSuccessHandler()) // redirecció segons rol
            .permitAll()
        )

        // Configuració del logout
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        );

        return http.build();
    }

    // Bean per encriptar contrasenyes amb BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
