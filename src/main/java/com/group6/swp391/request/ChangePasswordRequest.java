package com.group6.swp391.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {

    @NotNull(message = "Vui lòng nhập email or phone")
    @NotBlank(message = "Email or phone không được để trống")
    @Size(max = 255, min = 10, message = "Email or phone phải từ 10 tới 255 kí tự")
    String emailOrPhone;

    @NotNull(message = "Vui lòng nhập mật khẩu mới")
    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(max = 100, min = 6, message = "Mật khẩu phải từ 6 tới 100 kí tự")
    String newPassword;
}
