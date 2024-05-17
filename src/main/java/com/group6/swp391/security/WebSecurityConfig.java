package com.group6.swp391.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
//@EnableWebSecurity

public class WebSecurityConfig {

//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((auth) ->
//                auth.requestMatchers("/swp391/api/admin/**").permitAll()
//                        .requestMatchers("/swp391/api/manager/**").permitAll()
//                        .requestMatchers("/swp391/api/user/**").permitAll()
//                        .requestMatchers("/swp391/api/staff/**").permitAll()
////                              .requestMatchers(HttpMethod.POST, "/ues", "/*").permitAll()
//                        .anyRequest().authenticated());
////                .formLogin((login) -> login.loginPage("/login").loginProcessingUrl("/login").usernameParameter("name").passwordParameter("password")
////                        .defaultSuccessUrl("/admin"))
////                .logout((logout) -> logout.logoutUrl("/admin_logout").logoutSuccessUrl("/login"));
//        return httpSecurity.build();
//    }
}
