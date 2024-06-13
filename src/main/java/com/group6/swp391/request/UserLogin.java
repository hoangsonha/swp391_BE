package com.group6.swp391.request;

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
    @NotNull(message = "Email không hợp lệ")
    @NotBlank(message = "Email không hợp lệ")
    @Size(max = 100, min = 10, message = "Email phải từ 10 tới 100 kí tự bao gồm cả @gmail.com")
    private String email;

    @NotNull(message = "Mật khẩu không hợp lệ")
    @NotBlank(message = "Mật khẩu không hợp lệ")
    @Size(max = 100, min = 6, message = "Mật khẩu phải từ 6 tới 100 kí tự")
    private String password;

    private String recaptchaResponse;
}
