package com.group6.swp391.controller;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
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
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/swp391/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/all_users")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ObjectResponse> getAllUser() {
        List<User> lists = userService.findAll("staff");
        boolean check = false;
        if (lists != null) {
            if (lists.size() > 0) {
                check = true;
            }
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all users successfully", lists))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Failed", "Get all users failed", lists));
    }

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> adminRegister(@Valid @RequestBody UserRegister userRegister, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String randomString = UUID.randomUUID().toString();
        Role role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);

        boolean check = true;
        boolean active = false;

        User user = new User(null, null, userRegister.getEmail(), userRegister.getPassword(),
                null, null, null, randomString, false, true, role, 0, null, null, 0);
        if (userRegister == null || userService.getUserByEmail(userRegister.getEmail()) != null) {
            check = false;
        }

        if (check) {
            userService.save(user);
            if (!active) {
                String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
                check = userService.sendVerificationEmail(user, siteUrl);
            }
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create account successfully", user))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Create account failed", user));
    }

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<ObjectResponse> adminDeleteAccount(@PathVariable("id") int id) {
        boolean check = userService.deleteUser(id);
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete account successfully", null))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Delete account failed", null));
    }

}
