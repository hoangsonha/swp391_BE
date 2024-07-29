package com.group6.swp391.controllers;

import com.group6.swp391.config.CustomUserDetail;
import com.group6.swp391.pojos.User;
import com.group6.swp391.requests.PasswordRequest;
import com.group6.swp391.requests.UserInformation;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.services.UploadImageService;
import com.group6.swp391.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/swp391/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private UploadImageService uploadImageService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update/{id}")
    public ResponseEntity<ObjectResponse> userUpdate(@RequestBody UserInformation userInformation, @PathVariable("id") int id) {
        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(customUserDetail.getUserID() != id) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ObjectResponse("Failed", "Không được phép thay đổi thông tin do bạn không có quyền truy cập", null));
        }
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
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Thay đổi thông tin thành công", user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Thay đổi thông tin thất bại", null));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/change_password/{user_id}")
    public ResponseEntity<ObjectResponse> changePassWord(@PathVariable("user_id") int userId, @RequestBody @Valid PasswordRequest passwordRequest) {
        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(customUserDetail.getUserID() != userId) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ObjectResponse("Failed", "Không được phép thay đổi mật khẩu do bạn không có quyền truy cập", null));
        }
        try {
            User user = userService.getUserByID(userId);
            if(user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ObjectResponse("Failed", "Tài khoản không tồn tại", null));
            }
            if (!bCryptPasswordEncoder.matches(passwordRequest.getOldPassWord(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Failed", "Sai mật khẩu", null));
            }
            user.setPassword(bCryptPasswordEncoder.encode(passwordRequest.getNewPassWord()));
            userService.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Thay đổi mật khẩu thành công", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Thay đổi mật khẩu thất bại", null));
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') ")
    @GetMapping("/get/{id}")
    public ResponseEntity<ObjectResponse> getUser(@PathVariable("id") int id) {
        User user = userService.getUserByID(id);
        if(user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Lấy thông tin tài khoản thành công", user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Lấy thông tin tài khoản thất bại", null));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF') or hasRole('DELIVERY')")
    @PostMapping("/changeAvatar")
    public ResponseEntity<ObjectResponse> changeAvatar(@RequestParam("email") String email, @RequestParam("avatar") MultipartFile avatar) {
        try {
            String oldAvatar = userService.getUserByEmail(email).getAvata();
            String url = uploadImageService.upload(avatar); // Upload the image and get the URL
            User user = userService.getUserByEmail(email);
            user.setAvata(url);
            userService.save(user);
            uploadImageService.deleteImageOnFireBase(oldAvatar);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Đổi avatar tài khoản thành công", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Failed", "Đổi avatar tài khoản thất bại", null));
        }
    }

}
