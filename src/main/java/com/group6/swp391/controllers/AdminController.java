package com.group6.swp391.controllers;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.pojos.Role;
import com.group6.swp391.pojos.User;
import com.group6.swp391.requests.AdminRegister;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.services.RoleService;
import com.group6.swp391.services.UserService;
import io.jsonwebtoken.lang.Strings;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/swp391/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
//@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private RoleService roleService;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/all_users")
    public ResponseEntity<ObjectResponse> getAllUser() {
        List<User> lists = userService.findAll(EnumRoleName.ROLE_ADMIN.name());
        boolean check = false;
        if(lists !=null) if(lists.size() > 0) check = true;

        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Lấy danh sách người dùng và nhân viên thành công", lists))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Failed", "Lấy danh sách người dùng và nhân viên thất bại", lists));
    }

    @GetMapping("/all_role")
    public ResponseEntity<ObjectResponse> getAllRole() {
        List<Role> lists = roleService.getAllRoles();
        boolean check = false;
        if(lists !=null) if(lists.size() > 0) check = true;

        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Lấy tất cả quyền thành công", lists))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Failed", "Lấy tất cả quyền thất bại", lists));
    }

    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> adminRegister(@Valid @RequestBody AdminRegister adminRegister, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String randomString = UUID.randomUUID().toString();
        Role role = null;
        String role_register = adminRegister.getRole();
        if(role_register == null || !Strings.hasText(role_register)) {
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
                case "1":
                    role = roleService.getRoleByRoleName(EnumRoleName.ROLE_ADMIN);
                    break;
                default:
                    role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);
            }
        }
        boolean check = true;
        User user = new User(adminRegister.getFirstName(), adminRegister.getLastName(), adminRegister.getEmail(),
                bCryptPasswordEncoder.encode(adminRegister.getPassword()), adminRegister.getPhone(), adminRegister.getAddress(), adminRegister.getAvata(),
                randomString, false, true, role, 0, null, null, null, 0);
        if(userService.getUserByEmail(adminRegister.getEmail()) != null || adminRegister.getEmail() == null) {
            check = false;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Tạo tài khoản thất bại", null));
        }
        if(check) {
            userService.save(user);
            String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            check = userService.sendVerificationEmail(user, siteUrl);
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tạo tài khoản thành công", user))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Tạo tài khoản thất bại", null));
    }

    @PostMapping("/locked_user/{id}")
    public ResponseEntity<ObjectResponse> adminLockedAccount(@PathVariable("id") int id) {
        boolean check = userService.lockedUser(id);
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Khoá tài khoản thành công với ID: " + id, null))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Khoá tài khoản thành công với ID: " + id, null));
    }

    @PostMapping("/unlocked_user/{id}")
    public ResponseEntity<ObjectResponse> adminUnLockedAccount(@PathVariable("id") int id) {
        boolean check = userService.unLockedUser(id);
        User user = userService.getUserByID(id);
        if(user != null) {
            userService.setQuantityLoginFailed(0, user.getEmail());
        }
        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Mở khoá tài khoản thành công", null))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Mở khoá tài khoản thất bại", null));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ObjectResponse> adminDeleteAccount(@PathVariable("id") int id) {
        User user = userService.getUserByID(id);
        userService.lockedUserByEmail(user.getEmail());
        return  ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Xoá tài khoản thành công", null));
    }





//    @PostMapping("/delete/{id}")
//    public ResponseEntity<ObjectResponse> adminDeleteAcc(@RequestParam(defaultValue = "", value = "id", required = true) int id) {
//        User user = userService.getUserByID(id);
//        userService.lockedUserByEmail(user.getEmail());
//        return  ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Delete account successfully", null));
//    }


}
