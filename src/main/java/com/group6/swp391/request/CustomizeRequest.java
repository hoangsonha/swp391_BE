package com.group6.swp391.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomizeRequest {
    @NotBlank(message = "Mã sản phẩm không được để trống")
    String productId;

    @NotBlank(message = "Mã kim cương không được để trống")
    String diamondId;

    @Min(value = 1, message = "Kích thước phải lớn hơn hoặc bằng 1")
    int size;

    @Min(value = 0, message = "Tổng giá phải lớn hơn hoặc bằng 0")
    double totalPrice;

    public CustomizeRequest(String productId, String diamondId, int size, double totalPrice) {
        this.productId = productId;
        this.diamondId = diamondId;
        this.size = size;
        this.totalPrice = totalPrice;
    }
}
