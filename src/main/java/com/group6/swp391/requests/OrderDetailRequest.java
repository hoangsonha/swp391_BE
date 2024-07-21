package com.group6.swp391.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {
    int orderDetailID;
    int orderId;
    String ojectId;
    private int quantity;
    private double price;
}
