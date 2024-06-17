package com.group6.swp391.security;

import com.group6.swp391.jwt.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired private CustomUserDetailService customUserDetailService;
    @Autowired private CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }


    private final String[] REQUEST_PUBLIC = {"/login/**", "/verify/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((auth) ->
                auth.requestMatchers("/swp391/api/admin/**").permitAll()
                        .requestMatchers("/swp391/api/delivery/**").permitAll()
                        .requestMatchers("/swp391/api/user/**").permitAll()
                        .requestMatchers("/swp391/api/staff/**").permitAll()
                        .requestMatchers("/payment/**").permitAll()
                        .requestMatchers("/swp391/api/categories/**").permitAll()
                        .requestMatchers("/swp391/api/diamonds/**").permitAll()
                        .requestMatchers("/swp391/api/products/**").permitAll()
                        .requestMatchers("/swp391/api/carts/**").permitAll()
                        .requestMatchers("/swp391/api/productcustomes/**").permitAll()
                        .requestMatchers("/swp391/api/warrantycards/**").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/v3/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
//                              .requestMatchers(HttpMethod.POST, "/ues", "/*").permitAll()
                        .anyRequest().authenticated())

                .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  .oauth2Login(oauth2 -> oauth2.successHandler(customOAuth2AuthenticationSuccessHandler));
//                .logout(logout -> logout.logoutUrl("/logout").addLogoutHandler(logoutHandler).logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

//                .formLogin((login) -> login.loginPage("/login").loginProcessingUrl("/login").usernameParameter("name").passwordParameter("password")
//                        .defaultSuccessUrl("/admin"))

        return httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).build();
    }

    //    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
//        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
//        return daoAuthenticationProvider;
//    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
