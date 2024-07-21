package com.group6.swp391.requests;

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

    @NotNull(message = "Vui lòng nhập địa chỉ giao hàng")
    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    @Size(max = 300, min = 1, message = "Địa chỉ giao hàng phải từ 1 tới 300 kí tự")
    String addressShipping;

    @NotNull(message = "Vui lòng nhập họ và tên")
    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 50, min = 2, message = "Họ và tên phải từ 2 tới 50 kí tự")
    String fullName;

    @NotNull(message = "Vui lòng nhập số điện thoại giao hàng")
    @NotBlank(message = "Số điện thoại giao hàng không được để trống")
    @Size(min = 10, max = 12, message = "Số điện thoại phải có độ dài từ 10 đến 12 ký tự")
    String phoneShipping;

    @NotNull(message = "Vui lòng nhập giá")
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    double price;

    @NotNull(message = "Vui lòng nhập mã người dùng")
    @Min(value = 1, message = "Mã người dùng phải lớn hơn hoặc bằng 1")
    int userID;

    @Min(value = 0, message = "Điểm sử dụng phải lớn hơn hoặc bằng 0")
    double usedPoint;

    String note;

    @NotNull(message = "Vui lòng nhập email")
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    String email;

    @Min(value = 0, message = "Giảm giá phải lớn hơn hoặc bằng 0")
    double discount;

    EnumGenderName gender;
}
