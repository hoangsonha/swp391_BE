package com.group6.swp391.controller;

import com.group6.swp391.model.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.request.AdminRegister;
import com.group6.swp391.response.ObjectResponse;
import com.group6.swp391.service.RoleService;
import com.group6.swp391.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/swp391/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private RoleService roleService;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all_users")
    public ResponseEntity<ObjectResponse> getAllUser() {
        List<User> lists = userService.findAll("admin");
        boolean check = false;
        if(lists !=null) {
            if(lists.size() > 0) {
                check = true;
            }
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all users successfully", lists))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Failed", "Get_all users failed", lists));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> adminRegister(@RequestBody AdminRegister adminRegister, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String randomString = UUID.randomUUID().toString();
        Role role = null;
        String role_register = adminRegister.getRole();
        if(role_register == null) {
            role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);
        } else {
            switch (role_register) {
                case "4":
                    role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);
                    break;
                case "3":
                    role = roleService.getRoleByRoleName(EnumRoleName.ROLE_STAFF);
                    break;
                case "2":
                    role = roleService.getRoleByRoleName(EnumRoleName.ROLE_DELIVERY);
                    break;
            }
        }
        boolean check = true;
        boolean active = false;
        User user = new User(adminRegister.getFirstName(), adminRegister.getLastName(), adminRegister.getEmail(),
                bCryptPasswordEncoder.encode(adminRegister.getPassword()), adminRegister.getPhone(), adminRegister.getAddress(), adminRegister.getAvata(),
                randomString, active, true, role);
        if(userService.getUserByEmail(adminRegister.getEmail()) != null || adminRegister == null) {
            check = false;
        }

        if(check) {
            userService.save(user);
            if(!active) {
                String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
                check = userService.sendVerificationEmail(user, siteUrl);
            }
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Create account successfully", user))
                :ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ObjectResponse("Failed", "Create account failed", user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/locked_user/{id}")
    public ResponseEntity<ObjectResponse> adminLockedAccount(@PathVariable("id") int id) {
        boolean check = userService.lockedUser(id);
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Lock account successfully", null))
                :ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ObjectResponse("Failed", "Lock account failed", null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<ObjectResponse> adminDeleteAccount(@PathVariable("id") int id) {
        boolean check = userService.deleteUser(id);
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete account successfully", null))
                :ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ObjectResponse("Failed", "Delete account failed", null));
    }

}
