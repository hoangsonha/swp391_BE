package com.group6.swp391.request;

import com.group6.swp391.model.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Vui lòng nhập mã đơn hàng")
    @NotBlank(message = "Mã đơn hàng không được để trống")
    @Size(min = 1, message = "Mã đơn hàng phải lớn hơn 1 kí tự")
    private String orderID;

    @NotNull(message = "Vui lòng chọn phương thức thanh toán")
    @NotBlank(message = "Phương thức thanh toán không được để trống")
    @Size(max = 6, min = 5, message = "Phương thức thanh toán phải từ 5 tới 6 kí tự")
    private String paymentMethod;

    private boolean isDelivery;
}
