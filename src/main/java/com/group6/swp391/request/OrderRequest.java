package com.group6.swp391.request;

import com.group6.swp391.enums.EnumGenderName;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    String addressShipping;

    @NotBlank(message = "Họ và tên không được để trống")
    String fullName;

    @NotBlank(message = "Số điện thoại giao hàng không được để trống")
    @Size(min = 10, max = 15, message = "Số điện thoại phải có độ dài từ 10 đến 15 ký tự")
    String phoneShipping;

    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    double price;

    @NotNull(message = "Mã người dùng không được để trống")
    int userID;

    @Min(value = 0, message = "Điểm sử dụng phải lớn hơn hoặc bằng 0")
    double usedPoint;

    String note;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email;

    @Min(value = 0, message = "Giảm giá phải lớn hơn hoặc bằng 0")
    double discount;

    @NotNull(message = "Giới tính không được để trống")
    EnumGenderName gender;
}
