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
@NoArgsConstructor
@AllArgsConstructor
public class OTPValidationRequest {
    private String emailOrPhone;

    @NotNull(message = "OTP không hợp lệ")
    @NotBlank(message = "OTP không hợp lệ")
    @Size(max = 6, min = 6, message = "OTP phải nhập 6 kí tự")
    private String otp;
}
