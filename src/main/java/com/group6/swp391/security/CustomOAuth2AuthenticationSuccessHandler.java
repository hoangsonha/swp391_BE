package com.group6.swp391.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.swp391.jwt.JWTToken;
import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.RoleRepository;
import com.group6.swp391.repository.UserRepository;
import com.group6.swp391.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import java.io.IOException;
import java.util.UUID;

@Configuration
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private JWTToken jwtToken;

    @Value("${frontend.url}")
    private String urlRedirect;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//      this.setAlwaysUseDefaultTargetUrl(true);
//      this.setDefaultTargetUrl("http://localhost:3000/");
        User user = null;
        if(authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();
            user = userRepository.getUserByEmail(oauth2User.getAttribute("email").toString());
            if(user!=null){
                String token = jwtToken.generatedToken(CustomUserDetail.mapUserToUserDetail(user));

                response.setHeader("Authorization", "Bearer " + token);
                response.sendRedirect(urlRedirect);
                response.getWriter().write(new ObjectMapper().writeValueAsString(new TokenResponse("OK", "Login successfully", token)));
            } else {
                String randomString = UUID.randomUUID().toString();
                Role role = roleRepository.getRoleByRoleName(EnumRoleName.ROLE_USER);
                user = new User(null, oauth2User.getAttribute("name"), oauth2User.getAttribute("email"), null, null, null, null, randomString, true, true, role, 0, null, null, null);
                userRepository.save(user);
                CustomUserDetail customUserDetail = CustomUserDetail.mapUserToUserDetail(user);
                String token = jwtToken.generatedToken(customUserDetail);

                response.setHeader("Authorization", "Bearer " + token);
                response.sendRedirect(urlRedirect);
                response.getWriter().write(new ObjectMapper().writeValueAsString(new TokenResponse("Success", "Login account successfully", token)));
            }
        } else {
            response.sendRedirect(urlRedirect);
            response.getWriter().write(new ObjectMapper().writeValueAsString(new TokenResponse("Failed", "Login account failed", null)));
        }
    }



}