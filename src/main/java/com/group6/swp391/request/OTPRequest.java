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
public class OTPRequest {

    @NotNull(message = "Vui lòng nhập số điện thoại hoặc email")
    @NotBlank(message = "Số điện thoại hoặc email không được để trống")
    @Size(max = 255, min = 10, message = "Số điện thoại hoặc email phải từ 10 tới 255 kí tự")
    String emailOrPhone;
}
