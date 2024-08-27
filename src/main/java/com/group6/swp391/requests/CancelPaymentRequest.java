package com.group6.swp391.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CancelPaymentRequest {
    @NotBlank(message = "Mã order không được để trống")
    @NotNull(message = "Vui lòng nhập mã order")
    private String orderID;

    private String reason;
}
