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

    @NotNull(message = "Số điện thoại hoặc email không hợp lệ")
    @NotBlank(message = "Số điện thoại hoặc email không hợp lệ")
    @Size(max = 100, min = 10, message = "Số điện thoại hoặc email phải từ 10 tới 100 kí tự")
    String emailOrPhone;
}
