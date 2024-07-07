package com.group6.swp391.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartRequest {
    @NotNull(message = "Mã người dùng không được để trống")
    int userId;

    @NotBlank(message = "Mã sản phẩm không được để trống")
    String productId;
}
