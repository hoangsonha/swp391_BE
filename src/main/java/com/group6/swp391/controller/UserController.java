package com.group6.swp391.controller;

import com.group6.swp391.jwt.JWTToken;
import com.group6.swp391.model.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.request.UserInformation;
import com.group6.swp391.request.UserLogin;
import com.group6.swp391.request.UserRegister;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.response.TokenResponse;
import com.group6.swp391.security.CustomUserDetail;
import com.group6.swp391.service.RoleService;
import com.group6.swp391.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/swp391/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private RoleService roleService;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> userRegister(@RequestBody UserRegister userRegister, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Set<Role> roles = new HashSet<>();
        Role role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);
        roles.add(role);
        String randomString = UUID.randomUUID().toString();
        boolean check = true;

        User user = new User(null, null, userRegister.getEmail(), bCryptPasswordEncoder.encode(userRegister.getPassword()), null, null, null, randomString, false, true, roles);
        if(userRegister == null || userService.getUserByEmail(userRegister.getEmail()) != null) {
            check = false;
        }
        if(check) {
            userService.save(user);
            String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            check = userService.sendVerificationEmail(user, siteUrl);
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create account successfully", user))
                :ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ObjectResponse("Failed", "Create account failed", user));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update/{id}")
    public ResponseEntity<ObjectResponse> userRegister(@RequestBody UserInformation userInformation, @PathVariable("id") int id) {
        User user = userService.getUserByID(id);
        if(user != null) {
            user.setFirstName(userInformation.getFirstName());
            user.setLastName(userInformation.getLastName());
            user.setPassword(userInformation.getPassword());
            user.setAddress(userInformation.getAddress());
            user.setAvata(userInformation.getAvata());
            userService.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update account successfully", user));
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ObjectResponse("Failed", "Update account failed", null));
    }

}
