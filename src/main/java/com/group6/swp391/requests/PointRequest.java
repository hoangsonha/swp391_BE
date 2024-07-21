package com.group6.swp391.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PointRequest {
    @NotNull(message = "ID người dùng không được để trống")
    int userId;

    @NotNull(message = "ID đơn hàng không được để trống")
    int orderId;

    @NotNull(message = "Điểm sử dụng không được để trống")
    double usedPoint;
}
