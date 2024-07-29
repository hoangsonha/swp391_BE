package com.group6.swp391.controllers;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.pojos.Role;
import com.group6.swp391.pojos.User;
import com.group6.swp391.requests.UserRegister;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.services.RoleService;
import com.group6.swp391.services.UploadImageService;
import com.group6.swp391.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/swp391/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    @Autowired private UserService userService;
    @Autowired private RoleService roleService;
    @Autowired private UploadImageService uploadImageService;

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/all_users")
    public ResponseEntity<ObjectResponse> getAllUser() {
        List<User> lists = userService.findAll(EnumRoleName.ROLE_STAFF.name());
        boolean check = false;
        if (lists != null) if (lists.size() > 0) check = true;

        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Lấy toàn bộ người dùng thành công", lists))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Failed", "Lấy toàn bộ người dùng thất bại", lists));
    }

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> adminRegister(@Valid @RequestBody UserRegister userRegister, HttpServletRequest request) throws MessagingException, IOException {
        String randomString = UUID.randomUUID().toString();
        Role role = roleService.getRoleByRoleName(EnumRoleName.ROLE_USER);

        boolean check = true;
        User user = new User(null, null, userRegister.getEmail(), userRegister.getPassword(),null, null, null, randomString, false, true, role, 0, null, null, null, 0);

        if (userRegister == null || userService.getUserByEmail(userRegister.getEmail()) != null) check = false;

        String dataUrl = uploadImageService.generateImageWithInitial(userRegister.getEmail());
        String url = uploadImageService.uploadFileBase64(dataUrl);
        user.setAvata(url);

        if (check) {
            userService.save(user);
            String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            check = userService.sendVerificationEmail(user, siteUrl);
        }

        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tạo ra tài khoản người dùng thành công", user))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Tạo ra tài khoản người dùng thất bại", user));
    }

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<ObjectResponse> staffDeleteAccount(@PathVariable("id") int id) {
        boolean check = userService.lockedUser(id);

        return check ? ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Xoá tài khoản người dùng thành công", null))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Xoá tài khoản người dùng thất bại", null));
    }

}
