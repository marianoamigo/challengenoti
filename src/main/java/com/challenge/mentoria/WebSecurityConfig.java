package com.challenge.mentoria;

import com.challenge.mentoria.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(prePostEnabled = true)
    public class WebSecurityConfig {

        @Autowired
        public UserService userService;
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder());
        }
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    antMatcher("/"),
                                    antMatcher("/login"),
                                    antMatcher("/register"),
                                    antMatcher("/registration"),
                                    antMatcher("/success")
                            ).permitAll()
                            .anyRequest().authenticated()
                                    //.anyRequest().permitAll()
                    )
                    .formLogin(login -> login
                            .loginPage("/login")
                            .loginProcessingUrl("/logincheck")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .defaultSuccessUrl("/logged")
                            .permitAll()
                    )
                    .logout(logout -> logout
                                    .logoutUrl("/logout")
                                    .logoutSuccessUrl("/login?logout")
                                    .permitAll()
                            );
            return http.build();
        }
    }

