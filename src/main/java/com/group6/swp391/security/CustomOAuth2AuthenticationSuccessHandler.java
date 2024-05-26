package com.group6.swp391.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.swp391.jwt.JWTToken;
import com.group6.swp391.model.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.RoleRepository;
import com.group6.swp391.repository.UserRepository;
import com.group6.swp391.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Configuration
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private JWTToken jwtToken;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = null;
        if(authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oauth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();
            user = userRepository.getUserByEmail(oauth2User.getAttribute("email").toString());
            if(user!=null){
                String token = jwtToken.generatedToken(CustomUserDetail.mapUserToUserDetail(user));
                //more process you have to do in your logic
                response.getWriter().write(new ObjectMapper().writeValueAsString(new TokenResponse("OK", "Login successfully", token)));
                response.getWriter().flush();
            } else {
                String randomString = UUID.randomUUID().toString();
                Role role = roleRepository.getRoleByRoleName(EnumRoleName.ROLE_USER);
                user = new User(oauth2User.getAttribute("given_name"), oauth2User.getAttribute("family_name"), oauth2User.getAttribute("email"), null, null, null, oauth2User.getAttribute("picture"), randomString, oauth2User.getAttribute("email_verified"), true, role);
                userRepository.save(user);
                CustomUserDetail customUserDetail = CustomUserDetail.mapUserToUserDetail(user);
                String token = jwtToken.generatedToken(customUserDetail);
                response.getWriter().write(new ObjectMapper().writeValueAsString(new TokenResponse("Success", "Login account successfully", token)));
            }
        } else {
            response.getWriter().write(new ObjectMapper().writeValueAsString(new TokenResponse("Failed", "Login account failed", null)));
        }
    }

}