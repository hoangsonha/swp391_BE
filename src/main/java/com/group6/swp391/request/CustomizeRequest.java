package com.group6.swp391.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomizeRequest {

    @NotNull(message = "Vui lòng nhập mã sản phẩm")
    @NotBlank(message = "Mã sản phẩm không được để trống")
    @Size(min = 1, message = "Mã sản phẩm phải lớn hơn 1 kí tự")
    String productId;

    @NotNull(message = "Vui lòng nhập mã kim cương")
    @NotBlank(message = "Mã kim cương không được để trống")
    @Size(min = 1, message = "Mã kim cương phải lớn hơn 1 kí tự")
    String diamondId;

    @NotNull(message = "Vui lòng nhập kích thước")
    @NotBlank(message = "Kích thước không được để trống")
    @Min(value = 1, message = "Kích thước phải lớn hơn hoặc bằng 1")
    int size;

    private String collectionId;
}
