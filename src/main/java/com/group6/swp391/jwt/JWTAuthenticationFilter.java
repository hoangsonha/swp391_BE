package com.group6.swp391.jwt;

import com.group6.swp391.security.CustomUserDetail;
import com.group6.swp391.security.CustomUserDetailService;
import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired private JWTToken jwtToken;
    @Autowired private CustomUserDetailService customUserDetailService;

    public String getToken(HttpServletRequest request) {
        String s = request.getHeader("Authorization");
        if(s.startsWith("Bearer ") && StringUtils.hasText(s)) {
            return s.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String bearerToken = getToken(request);
            if(Strings.hasText(bearerToken) && jwtToken.validate(bearerToken)) {
                String email = jwtToken.getEmailFromJwt(bearerToken);
                CustomUserDetail customUserDetail = (CustomUserDetail) customUserDetailService.loadUserByUsername(email);
                if(customUserDetail != null) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(customUserDetail, null, customUserDetail.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } catch (Exception e) {
            log.error("Fail on set user authentication:" + e.toString());
        }
        filterChain.doFilter(request, response);
    }
}
