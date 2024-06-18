package com.group6.swp391.request;

import com.group6.swp391.model.Payment;
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
    //List<PaymentRequest> payments;
    String phoneShipping;
    double price;
    int quantity;
    String status;
    User user;
    List<OrderDetailRequest> orderDetail;
}
