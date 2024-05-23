package com.group6.swp391.controller;

import com.group6.swp391.jwt.JWTToken;
import com.group6.swp391.logout.ListToken;
import com.group6.swp391.request.UserLogin;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.response.TokenResponse;
import com.group6.swp391.security.CustomUserDetail;
import com.group6.swp391.service.RoleService;
import com.group6.swp391.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/public")
@Slf4j
public class MainController {
    @Autowired private UserService userService;
    @Autowired private RoleService roleService;
    @Autowired private JWTToken jwtToken;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private ListToken listToken;

    @GetMapping("/verify")
    public ResponseEntity<ObjectResponse> verifyAccount(@Param("code") String code, Model model) {
        boolean check = userService.verifyAccount(code);
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Verify account successfully", null))
                :ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ObjectResponse("Failed", "Verify account failed", null));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginPage(@RequestBody UserLogin userLogin) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());

            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String s = jwtToken.generatedToken(userDetails);
            boolean check = jwtToken.validate(s);
            return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse("Success", "Login successfully", s));
        } catch(Exception e) {
            log.error("Cannot login : {}", e.toString());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TokenResponse("Failed", "Login failed", null));
    }

    @PostMapping("/logout")
    public ResponseEntity<ObjectResponse> getLogout(HttpServletRequest request) {
        String token = getToken(request);
        listToken.addToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Logout successfully", null));
    }

    public String getToken(HttpServletRequest request) {
        String s = request.getHeader("Authorization");
        if(s.startsWith("Bearer ") && StringUtils.hasText(s)) {
            return s.substring(7);
        }
        return null;
    }
}