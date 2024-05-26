package com.group6.swp391.controller;

import com.group6.swp391.jwt.JWTToken;
import com.group6.swp391.logout.ListToken;
import com.group6.swp391.request.OTPRequest;
import com.group6.swp391.request.OTPValidationRequest;
import com.group6.swp391.request.UserLogin;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.response.TokenResponse;
import com.group6.swp391.security.CustomUserDetail;
import com.group6.swp391.service.RoleService;
import com.group6.swp391.service.UserService;
import com.group6.swp391.sms.SpeedSMSAPI;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;


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

    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/verify")
    public ResponseEntity<ObjectResponse> verifyAccount(@Param("code") String code, Model model) {
        boolean check = userService.verifyAccount(code);
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Verify account successfully", null))
                :ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Verify account failed", null));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginPage(@RequestBody UserLogin userLogin, HttpServletRequest request, HttpServletResponse response) {
        try {
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
            boolean check_captcha = userService.verifyRecaptcha(gRecaptchaResponse);
            if(check_captcha) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());

                Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

                CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String s = jwtToken.generatedToken(userDetails);
                boolean check = jwtToken.validate(s);
                return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse("Success", "Login successfully", s));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TokenResponse("Failed", "Login failed", null));
            }
        } catch(Exception e) {
            logger.error("Cannot login : {}", e.toString());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TokenResponse("Failed", "Login failed", null));
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

    @PostMapping("/forget_password")
    public ResponseEntity<ObjectResponse> otpPage(@RequestBody OTPRequest otpRequest) {
        boolean check = userService.sendSMS(otpRequest);
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Login successfully", null))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Login failed", null));
    }

    @PostMapping("/validationOTP")
    public ResponseEntity<ObjectResponse> otpPage(@RequestBody OTPValidationRequest otpValidationRequest) {
       boolean check = userService.validateOTP(otpValidationRequest);
       return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Login successfully", null))
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Login failed", null));
    }


//=-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//    @PostMapping("/login_google")
//    public ResponseEntity<TokenResponse> userLoginWithGoolge(@AuthenticationPrincipal OAuth2User user, HttpServletResponse response) throws IOException {
//        User us = userService.getUserByEmail(user.getAttribute("email"));
//        if(us == null) {
//            String randomString = UUID.randomUUID().toString();
//            Set<Role> roles = new HashSet<>();
//            Role role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);
//            roles.add(role);
//            us = new User(user.getAttribute("given_name"), user.getAttribute("family_name"), user.getAttribute("email"), null, null, null, user.getAttribute("picture"), randomString, user.getAttribute("email_verified"), true, roles);
//            userService.save(us);
//            CustomUserDetail customUserDetail = CustomUserDetail.mapUserToUserDetail(us);
//            String s = jwtToken.generatedToken(customUserDetail);
//            return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse("Success", "Login account successfully", s));
//        }
//        CustomUserDetail customUserDetail = CustomUserDetail.mapUserToUserDetail(us);
//        String s = jwtToken.generatedToken(customUserDetail);
//
//        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//
//        return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse("Success", "Login account successfully", s));
//    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


}
