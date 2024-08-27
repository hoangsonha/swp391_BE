package com.group6.swp391.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartRequest {
    @NotNull(message = "Vui lòng nhập mã người dùng")
    @Min(value = 1, message = "Mã người dùng phải lớn hơn 0")
    int userId;

    @NotBlank(message = "Mã sản phẩm không được để trống")
    @NotNull(message = "Vui lòng nhập mã sản phẩm")
    @Min(value = 1, message = "Mã sản phẩm phải lớn hơn 0")
    String productId;
}
