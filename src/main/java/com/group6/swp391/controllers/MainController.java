package com.group6.swp391.controllers;

import com.group6.swp391.enums.EnumTokenType;
import com.group6.swp391.jwt.JWTToken;
import com.group6.swp391.logout.ListToken;
import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.mapper.UserMapper;
import com.group6.swp391.pojos.Diamond;
import com.group6.swp391.pojos.Product;
import com.group6.swp391.pojos.Role;
import com.group6.swp391.pojos.User;
import com.group6.swp391.requests.*;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.responses.TokenResponse;
import com.group6.swp391.config.CustomUserDetail;
import com.group6.swp391.services.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/public")
@Slf4j
public class MainController {

    @Value("${time.reset}")
    private int timeReset;

    @Autowired private UserService userService;
    @Autowired private RoleService roleService;
    @Autowired private JWTToken jwtToken;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private ListToken listToken;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private DiamondService diamondService;
    @Autowired private ProductService productService;
    @Autowired private SearchService searchService;
    @Autowired private UploadImageService uploadImageService;

    @Value("${frontend.url}")
    private String urlRedirect;

    @GetMapping("/all_users")
    public List<User> getAll() {
        return userService.findAll("admin");
    }

    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> userRegister(@Valid @RequestBody UserRegister userRegister, HttpServletRequest request) throws MessagingException, IOException {
        Role role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);
        String randomString = UUID.randomUUID().toString();
        boolean check = true;
        User user = new User(null, null, userRegister.getEmail(), bCryptPasswordEncoder.encode(userRegister.getPassword()), null, null, null, randomString, false, true, role, 0, null, null, null, 0);
        if(userRegister == null || userService.getUserByEmail(userRegister.getEmail()) != null) {
            check = false;
        }

        String dataUrl = uploadImageService.generateImageWithInitial(userRegister.getEmail());
        String url = uploadImageService.uploadFileBase64(dataUrl);
        user.setAvata(url);

        if(check) {
            userService.save(user);
            String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            check = userService.sendVerificationEmail(user, siteUrl);
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create account successfully", user))
                :ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Create account failed", null));
    }

    @GetMapping("/verify")
    public void verifyAccount(@RequestParam(name = "code", required = true) String code, HttpServletResponse response) { // @Param sử dụng trong JPA để set value cho biến trong query
        boolean check = false;
        try {
            check = userService.verifyAccount(code);
            if(check) response.sendRedirect(urlRedirect);
        } catch(Exception e) {
            log.error("Can not verify account");
        }
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        if(StringUtils.hasText(refreshToken) && !listToken.isListToken(refreshToken)) {
            if(jwtToken.validate(refreshToken, EnumTokenType.REFRESH_TOKEN)) {
                String email = jwtToken.getEmailFromJwt(refreshToken, EnumTokenType.REFRESH_TOKEN);
                CustomUserDetail customUserDetail = CustomUserDetail.mapUserToUserDetail(userService.getUserByEmail(email));
                if(customUserDetail != null) {
                    String newToken = jwtToken.generatedToken(customUserDetail);
                    return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse("Success", "Refresh token successfully", newToken, refreshToken));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TokenResponse("Failed", "Refresh token failed", null, null));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginPage(@Valid @RequestBody UserLogin userLogin) {
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
                String token = jwtToken.generatedToken(userDetails);
                String refreshToken = jwtToken.generatedRefreshToken(userDetails);
//                User ss = UserMapper.customUserDetailToUser(userDetails);
                userService.setQuantityLoginFailed(0, userDetails.getEmail());
                userService.setTimeOffline(null, userDetails.getEmail());
                userService.setQuantityReceiveEmailOffline(0, userDetails.getEmail());

                return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse("Success", "Login successfully", token, refreshToken));
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
                            if(minus >= timeReset) {
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
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "your account is locked. Please contact to our admin to unlock", null, null));
                        }
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "You have " + remainingAttempt + " password attempts left before your account is locked", null, null));
                    } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "your account is locked. Please contact to our admin to unlock", null, null));
                } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "your account is not active. Please check your email for verify account", null, null));
            } else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("Failed", "Your Email isn't exist. Please register it", null, null));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<ObjectResponse> getLogout(HttpServletRequest request) {
        String token = getToken(request);
        String refreshToken = request.getHeader("RefreshToken");
        String email = jwtToken.getEmailFromJwt(token, EnumTokenType.TOKEN);
        listToken.addToken(token);
        listToken.addToken(refreshToken);
        userService.setTimeOffline(new Date(), email);
        return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Logout successfully", null));
    }

//    @PostMapping("/logout")
//    public ResponseEntity<ObjectResponse> getLogout(HttpServletRequest request) {
//        String token = getToken(request);
//        String email = jwtToken.getEmailFromJwt(token, EnumTokenType.TOKEN);
//        listToken.addToken(token);
//        userService.setTimeOffline(new Date(), email);
//        return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Logout successfully", null));
//    }

    private String getToken(HttpServletRequest request) {
        String s = request.getHeader("Authorization");
        if(s.startsWith("Bearer ") && StringUtils.hasText(s)) {
            return s.substring(7);
        }
        return null;
    }

    @PostMapping("/forget_password")
    public ResponseEntity<ObjectResponse> getForgetPassword(@Valid @RequestBody OTPRequest otpRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
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
    public ResponseEntity<ObjectResponse> getDiamondBySearchAdvanced(@RequestBody @Valid SearchAdvanceRequest searchAdvanceRequest) {
        List<Diamond> lists = diamondService.searchAdvanced(searchAdvanceRequest);
        return lists.size() > 0 ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all diamond by search advance successfully", lists))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Get all diamond by search advance failed", null));
    }

    @GetMapping("/search_advanced_specification")
    public ResponseEntity<ObjectResponse> searchAdvancedSpecification(@RequestBody @Valid SearchAdvanceRequest searchAdvanceRequest) {
        List<Diamond> lists = diamondService.searchAdvancedSpecification(searchAdvanceRequest);
        return lists.size() > 0 ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all diamond by search advance successfully", lists))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Get all diamond by search advance failed", null));
    }

    /**
     * Method get all product
     * @return list product
     */
    @GetMapping("/all_products")
    public ResponseEntity<ObjectResponse> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            if(products == null || products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Danh sách sản phẩm trống", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Danh sách sản phẩm", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    @GetMapping("/all_diamonds")
    public ResponseEntity<ObjectResponse> getAllDiamonds() {
        List<Diamond> diamonds;
        try {
            diamonds = diamondService.getAllDiamond();
            if (diamonds == null || diamonds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Danh sách kim cương trống", null));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Danh sách kim ương", diamonds));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }


    @GetMapping("/search")
    public ResponseEntity<ObjectResponse> search(@RequestParam String query) {
        try {
            String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
            List<Object> results = searchService.search(decodedQuery);
            if (results.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ObjectResponse("Failed", "Không tìm thấy kết quả nào", null));
            } else {
                return ResponseEntity.ok(new ObjectResponse("Success", "Lấy kết quả tìm kiếm thành công", results));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Lấy kết quả tìm kiếm không thành công", e.getMessage()));
        }
    }


    @GetMapping("/product/{product_id}")
    public ResponseEntity<ObjectResponse> getProductById(@PathVariable("product_id") String productID) {
        try {
            Product product = productService.getProductById(productID);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Sản phẩm không tồn tại", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Lấy sản phẩm thành công", product));

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Lấy sản phẩm thất bại", null));
        }
    }


    @GetMapping("/diamond_id/{diamond_id}")
    public ResponseEntity<ObjectResponse> getDiamondByDiamondID(@PathVariable("diamond_id") String diamondID) {
        try {
            Diamond existingDiamond = diamondService.getDiamondByDiamondID(diamondID);
            if( existingDiamond == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Kim cương không tồn tại", null));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Kim Cương với Id " + diamondID, existingDiamond));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
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
