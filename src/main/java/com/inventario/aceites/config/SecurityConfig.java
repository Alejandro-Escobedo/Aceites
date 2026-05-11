package com.inventario.aceites.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager users() {

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("Alejandro")
                .password("250897")
                .roles("ADMIN")
                .build();

        UserDetails operador1 = User.withDefaultPasswordEncoder()
                .username("Jeancarlo")
                .password("123456")
                .roles("OPERADOR")
                .build();

        UserDetails operador2 = User.withDefaultPasswordEncoder()
                .username("Victor")
                .password("123456")
                .roles("OPERADOR")
                .build();

        UserDetails visor = User.withDefaultPasswordEncoder()
                .username("willy")
                .password("123456")
                .roles("VISOR")
                .build();

        return new InMemoryUserDetailsManager(
                admin,
                operador1,
                operador2,
                visor
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/img/**").permitAll()

                        .requestMatchers("/login").permitAll()

                        .requestMatchers("/inventario/**").authenticated()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form

                        .loginPage("/login")

                        .defaultSuccessUrl("/inventario", true)

                        .permitAll()
                )

                .logout(logout -> logout
                        .permitAll()
                );

        return http.build();
    }
}