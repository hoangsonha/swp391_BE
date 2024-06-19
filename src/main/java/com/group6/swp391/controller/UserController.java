package com.group6.swp391.controller;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.request.UserInformation;
import com.group6.swp391.request.UserRegister;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.service.RoleService;
import com.group6.swp391.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
@RequestMapping("/swp391/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private RoleService roleService;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> userRegister(@Valid @RequestBody UserRegister userRegister, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Role role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);

        String randomString = UUID.randomUUID().toString();
        boolean check = true;

        User user = new User(null, null, userRegister.getEmail(), bCryptPasswordEncoder.encode(userRegister.getPassword()), null, null, null, randomString, false, true, role, 0, null, null, null);
        if(userRegister == null || userService.getUserByEmail(userRegister.getEmail()) != null) {
            check = false;
        }
        if(check) {
            userService.save(user);
            String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            // http://localhost:8080
            check = userService.sendVerificationEmail(user, siteUrl);
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create account successfully", user))
                :ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Create account failed", user));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update/{id}")
    public ResponseEntity<ObjectResponse> userUpdate(@RequestBody UserInformation userInformation, @PathVariable("id") int id) {
        User user = userService.getUserByID(id);
        if(user != null) {
            user.setFirstName(userInformation.getFirstName());
            user.setLastName(userInformation.getLastName());
            user.setPassword(bCryptPasswordEncoder.encode(userInformation.getPassword()));
            user.setAddress(userInformation.getAddress());
            user.setAvata(userInformation.getAvata());
            user.setPhone(userInformation.getPhoneNumber());
            user.setGender(userInformation.getGender());
            user.setYearOfBirth(user.getYearOfBirth());
            userService.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update account successfully", user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Update account failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/{id}")
    public ResponseEntity<ObjectResponse> getUser(@PathVariable("id") int id) {
        User user = userService.getUserByID(id);
        if(user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get account successfully", user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Get account failed", null));
    }

}
