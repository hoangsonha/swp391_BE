package com.group6.swp391.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordRequest {

    @NotBlank(message = "Mật khẩu cũ không được để trống")
    String oldPassWord;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    String newPassWord;
}
