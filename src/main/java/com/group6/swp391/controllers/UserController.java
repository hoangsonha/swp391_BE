package com.group6.swp391.controllers;

import com.group6.swp391.pojos.User;
import com.group6.swp391.requests.PasswordRequest;
import com.group6.swp391.requests.UserInformation;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.services.RoleService;
import com.group6.swp391.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/swp391/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private RoleService roleService;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update/{id}")
    public ResponseEntity<ObjectResponse> userUpdate(@RequestBody UserInformation userInformation, @PathVariable("id") int id) {
        User user = userService.getUserByID(id);
        if(user != null) {
            if(userInformation.getFirstName() != null) {
                user.setFirstName(userInformation.getFirstName());
            }
            if(userInformation.getLastName() != null) {
                user.setLastName(userInformation.getLastName());
            }
            if(userInformation.getAddress() != null) {
                user.setAddress(userInformation.getAddress());
            }
            if(userInformation.getPhoneNumber() != null) {
                user.setPhone(userInformation.getPhoneNumber());
            }
            if(userInformation.getGender() != null) {
                user.setGender(userInformation.getGender());
            }
            if(userInformation.getAvata() != null) {
                user.setAvata(userInformation.getAvata());
            }
            if(userInformation.getYearOfBirth() != null) {
                user.setYearOfBirth(user.getYearOfBirth());
            }
            userService.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update account successfully", user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Update account failed", null));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/change_password/{user_id}")
    public ResponseEntity<ObjectResponse> changePassWord(@PathVariable("user_id") int userId,
                                                         @RequestBody @Valid PasswordRequest passwordRequest) {
        try {
            User user = userService.getUserByID(userId);
            if(user == null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Failed", "User Not Existing", null));
            }
            if (!bCryptPasswordEncoder.matches(passwordRequest.getOldPassWord(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Failed", "Incorrect password", null));
            }
            user.setPassword(bCryptPasswordEncoder.encode(passwordRequest.getNewPassWord()));
            userService.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Update password successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Failed", "Update password failed", null));
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') ")
    @GetMapping("/get/{id}")
    public ResponseEntity<ObjectResponse> getUser(@PathVariable("id") int id) {
        User user = userService.getUserByID(id);
        if(user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get account successfully", user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Get account failed", null));
    }

}
