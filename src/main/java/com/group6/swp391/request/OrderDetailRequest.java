package com.group6.swp391.request;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Order;
import com.group6.swp391.model.ProductCustomize;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {
    int orderDetailID;
    Order order;
    String productCustomizeID;
    String diamondID;
    private int quantity;
    private double price;
}
