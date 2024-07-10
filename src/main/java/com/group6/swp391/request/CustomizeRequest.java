package com.group6.swp391.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomizeRequest {
    @NotBlank(message = "Mã sản phẩm không được để trống")
    String productId;

    @NotBlank(message = "Mã kim cương không được để trống")
    String diamondId;

    @Min(value = 1, message = "Kích thước phải lớn hơn hoặc bằng 1")
    int size;
    private String collectionId;
}
