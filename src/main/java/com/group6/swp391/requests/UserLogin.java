package com.group6.swp391.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {

    @Email(message = "Email không hợp lệ")
    @NotNull(message = "Vui lòng điền email")
    @NotBlank(message = "Email không được để trống")
    @Size(max = 255, min = 10, message = "Email phải từ 10 tới 255 kí tự bao gồm cả @gmail.com")
    private String email;

    @NotNull(message = "Vui lòng nhập mật khẩu")
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(max = 100, min = 6, message = "Mật khẩu phải từ 6 tới 100 kí tự")
    private String password;

    private String recaptchaResponse;
}
