package com.group6.swp391.controller;

import com.group6.swp391.model.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.request.UserRegister;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.service.RoleService;
import com.group6.swp391.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private RoleService roleService;

    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> userRegister(@RequestBody UserRegister userRegister, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Set<Role> roles = new HashSet<>();
        Role role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);
        roles.add(role);
        String randomString = UUID.randomUUID().toString();
        boolean check = true;

        User user = new User(userRegister.getFirstName(), userRegister.getLastName(), userRegister.getEmail(), userRegister.getPassword(), userRegister.getPhone(), userRegister.getAddress(), userRegister.getAvata(), randomString, false, true, roles);
        if(userRegister==null || userService.getUserByEmail(userRegister.getEmail()) != null) {
            check = false;
        }
        if(check) {
            userService.save(user);
            String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            check = userService.sendVerificationEmail(user, siteUrl, "user");
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create account successfully", user))
                :ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ObjectResponse("Failed", "Create account failed", user));
    }

    @GetMapping("/verify")
    public ResponseEntity<ObjectResponse> verifyAccount(@Param("code") String code, Model model) {
        boolean check = userService.verifyAccount(code);
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Verify account successfully", null))
                :ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ObjectResponse("Failed", "Verify account failed", null));
    }


}
