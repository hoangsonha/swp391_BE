package com.group6.swp391.controller;

import com.group6.swp391.model.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.model.User;
import com.group6.swp391.request.AdminRegister;
import com.group6.swp391.request.ManagerRegister;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("swp391/api/manager")
@CrossOrigin(origins = "*")
public class ManagerController {
    @Autowired private UserService userService;
    @Autowired private RoleService roleService;

    @GetMapping("/all_users")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ObjectResponse> getAllUser() {
        List<User> lists = userService.findAll("manager");
        boolean check = false;
        if(lists !=null) {
            if(lists.size() > 0) {
                check = true;
            }
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Get all users successfully", lists))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Failed", "Get all users failed", lists));
    }

    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> adminRegister(@RequestBody ManagerRegister managerRegister, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String randomString = UUID.randomUUID().toString();
        Set<Role> roles = new HashSet<>();
        Set<String> roles_register = managerRegister.getRoles();
        if(roles_register == null) {
            Role role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);
            roles.add(role);
        } else {
            roles_register.forEach(role -> {
                switch (role) {
                    case "user":
                        Role user_role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);
                        roles.add(user_role);
                    case "staff":
                        Role staff_role = roleService.getRoleByRoleName(EnumRoleName.ROLE_STAFF);
                        roles.add(staff_role);
                }
            });
        }

        boolean check = true;
        boolean active = false;

        User user = new User(managerRegister.getFirstName(), managerRegister.getLastName(), managerRegister.getEmail(), managerRegister.getPassword(), managerRegister.getPhone(), managerRegister.getAddress(), managerRegister.getAvata(), randomString, active, true, roles);
        if(userService.getUserByEmail(managerRegister.getEmail()) != null || managerRegister == null ) {
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

}
