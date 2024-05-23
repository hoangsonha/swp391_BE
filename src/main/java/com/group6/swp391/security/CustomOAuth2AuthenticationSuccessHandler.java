package com.group6.swp391.security;

import com.group6.swp391.jwt.JWTToken;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
public class CustomOAuth2AuthenticationSuccessHandler extends
        SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTToken jwtToken;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException,
            ServletException {

        if(authentication instanceof OAuth2AuthenticationToken){
            OAuth2User oauth2User = ((OAuth2AuthenticationToken)
                    authentication).getPrincipal();
            User user = userRepository.
                    getUserByEmail(oauth2User.getAttribute("email").toString());
            if(user!=null){
                System.out.println("customer exist");
                String token = jwtToken.generatedToken(CustomUserDetail.mapUserToUserDetail(user));
                //more process you have to do in your logic
                response.getWriter().write(token);
                response.getWriter().flush();

            } else {
                //customer doesn't exist, throw an error or create new one
            }
        } else {
            //error
        }

    }
}