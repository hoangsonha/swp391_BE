package com.group6.swp391.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OTPValidationRequest {

    @NotNull(message = "Vui lòng nhập số điện thoại hoặc email")
    @NotBlank(message = "Số điện thoại hoặc email không được để trống")
    @Size(max = 255, min = 10, message = "Số điện thoại hoặc email phải từ 10 tới 255 kí tự")
    private String emailOrPhone;

    @NotNull(message = "Vui lòng nhập OTP")
    @NotBlank(message = "OTP không được để trống")
    @Size(max = 6, min = 6, message = "OTP phải nhập 6 kí tự")
    String otp;
}
