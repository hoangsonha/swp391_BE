package com.group6.swp391.config;

import com.group6.swp391.jwt.JWTToken;
import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.pojos.Role;
import com.group6.swp391.pojos.User;
import com.group6.swp391.repositories.RoleRepository;
import com.group6.swp391.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
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

                String urlToken = urlRedirect + "?token=" + token;
                response.sendRedirect(urlToken);
            } else {
                String randomString = UUID.randomUUID().toString();
                Role role = roleRepository.getRoleByRoleName(EnumRoleName.ROLE_USER);
                user = new User(null, oauth2User.getAttribute("name"), oauth2User.getAttribute("email"), null, null, null, null, randomString, true, true, role, 0, null, null, null, 0);
                userRepository.save(user);
                CustomUserDetail customUserDetail = CustomUserDetail.mapUserToUserDetail(user);
                String token = jwtToken.generatedToken(customUserDetail);

                String urlToken = urlRedirect + "?token=" + token;
                response.sendRedirect(urlToken);
            }
        } else {
            response.sendRedirect(urlRedirect);
        }
    }


}