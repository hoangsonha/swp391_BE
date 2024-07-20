package com.group6.swp391.controller;

import com.group6.swp391.jwt.JWTToken;
import com.group6.swp391.logout.ListToken;
import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.request.*;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.response.TokenResponse;
import com.group6.swp391.security.CustomUserDetail;
import com.group6.swp391.service.DiamondService;
import com.group6.swp391.service.ProductService;
import com.group6.swp391.service.RoleService;
import com.group6.swp391.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private DiamondService diamondService;
    @Autowired private ProductService productService;

    @Value("${frontend.url}")
    private String urlRedirect;

    @GetMapping("/all_users")
    public List<User> getAll() {
        return userService.findAll("admin");
    }

    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> userRegister(@Valid @RequestBody UserRegister userRegister, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Role role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);
        String randomString = UUID.randomUUID().toString();
        boolean check = true;
        User user = new User(null, null, userRegister.getEmail(), bCryptPasswordEncoder.encode(userRegister.getPassword()), null, null, null, randomString, false, true, role, 0, null, null, null, 0);
        if(userRegister == null || userService.getUserByEmail(userRegister.getEmail()) != null) {
            check = false;
        }
        if(check) {
            userService.save(user);
            String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            check = userService.sendVerificationEmail(user, siteUrl);
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create account successfully", user))
                :ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Create account failed", null));
    }

    @GetMapping("/verify")
    public void verifyAccount(@Param("code") String code, Model model, HttpServletResponse response) throws IOException {
        boolean check = false;
        try {
            check = userService.verifyAccount(code);
            if(check) response.sendRedirect(urlRedirect);
        } catch(Exception e) {
            log.error("Can not verify account");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginPage(@Valid @RequestBody UserLogin userLogin, HttpServletRequest request, HttpServletResponse response) {
        try {
//            String gRecaptchaResponse = userLogin.getRecaptchaResponse();
//            boolean check_captcha = userService.verifyRecaptcha(gRecaptchaResponse);
//            if(check_captcha) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());
                Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
                CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String s = jwtToken.generatedToken(userDetails);

                userService.setQuantityLoginFailed(0, userDetails.getEmail());
                userService.setTimeOffline(null, userDetails.getEmail());
                userService.setQuantityReceiveEmailOffline(0, userDetails.getEmail());

                return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse("Success", "Login successfully", s));
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Login failed", null));
//            }
        } catch(Exception e) {
            log.error("Cannot login : {}", e.toString());
            User user = userService.getUserByEmail(userLogin.getEmail());
            if (user != null) {
                if(user.isEnabled()) {
                    if(user.isNonLocked() == true) {
                        int quantityLoginFailed = user.getQuantityLoginFailed();
                        if(quantityLoginFailed == 0) {
                            userService.setTimeLoginFailed(new Date(), user.getEmail());
                        } else {
                            long minus = userService.calculateSecondInMinute(user);
                            if(minus >= 300) {
                                userService.setQuantityLoginFailed(1, user.getEmail());
                                quantityLoginFailed = 0;
                                userService.setTimeLoginFailed(new Date(), user.getEmail());
                            }
                        }
                        if(quantityLoginFailed == 5) {
                            userService.lockedUserByEmail(userLogin.getEmail());
                        } else userService.setQuantityLoginFailed((quantityLoginFailed + 1), userLogin.getEmail());
                        int remainingAttempt = (5-quantityLoginFailed);
                        if(remainingAttempt == 0) {
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "your account is locked. Please contact to our admin to unlock", null));
                        }
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "You have " + remainingAttempt + " password attempts left before your account is locked", null));
                    } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "your account is locked. Please contact to our admin to unlock", null));
                } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "your account is not active. Please check your email for verify account", null));
            } else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Your Email isn't exist. Please register it", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ObjectResponse> getLogout(HttpServletRequest request) {
        String token = getToken(request);
        String email = jwtToken.getEmailFromJwt(token);
        listToken.addToken(token);
        userService.setTimeOffline(new Date(), email);
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
    public ResponseEntity<ObjectResponse> getForgetPassword(@Valid @RequestBody OTPRequest otpRequest, HttpServletRequest request, HttpServletResponse response) throws MessagingException, UnsupportedEncodingException {
        boolean checkOTPRequest = userService.checkEmailOrPhone(otpRequest.getEmailOrPhone());
        String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
        boolean check = false;
        if(checkOTPRequest) {
            check = userService.sendResetPasswordEmail(otpRequest, siteUrl);
        } else {
            check = userService.sendSMS(otpRequest);
        }
        return check ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Sent otp successfully", null))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Sent OTP failed due to the phone or email hasn't register", null));
    }

    @PostMapping("/validationOTP")
    public ResponseEntity<ObjectResponse> getValidationOTP(@Valid @RequestBody OTPValidationRequest otpValidationRequest) {
        boolean check = userService.validateOTP(otpValidationRequest);
        return check ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Authentication successfully", null))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Authentication failed", null));
    }

    @PostMapping("/set_password")
    public ResponseEntity<ObjectResponse> SetPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        boolean check = userService.setNewPassword(changePasswordRequest.getEmailOrPhone(), changePasswordRequest.getNewPassword());
        return check ?
                ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Change password successfully", null))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Change password failed", null));
    }

    @GetMapping("/search_advanced")
    public ResponseEntity<ObjectResponse> getDiamondBySearchAdvanced(@RequestBody SearchAdvanceRequest searchAdvanceRequest) {
        List<Diamond> lists = diamondService.searchAdvanced(searchAdvanceRequest);
        return lists.size() > 0 ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all diamond by search advance successfully", lists))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Get all diamond by search advance failed", null));
    }


    /**
     * Method get all product
     * @return list product
     */
    @GetMapping("/all_products")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/all_diamonds")
    public ResponseEntity<?> getAllDiamonds() {
        List<Diamond> diamonds;
        try {
            diamonds = diamondService.getAllDiamond();
            if (diamonds == null || diamonds.isEmpty()) {
                throw new RuntimeException("Diamond list is empty!");
            } else {
                return ResponseEntity.ok(diamonds);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get all diamonds failed");
        }
    }




//    private static final String USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
//
//    @GetMapping("/userinfo")
//    public String getUserInfo(@RequestParam("access_token") String accessToken) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                USERINFO_URL,
//                HttpMethod.GET,
//                entity,
//                String.class
//        );
//        return response.getBody();
//    }
}
