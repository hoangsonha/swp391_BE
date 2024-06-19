package com.group6.swp391.request;

import com.group6.swp391.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String addressShipping;
    String fullName;
    LocalDateTime orderDate;
    String phoneShipping;
    double price;
    String status;
    int quantity;
    User user;
    double usedPoint;
    List<OrderDetailRequest> orderDetail;
}
