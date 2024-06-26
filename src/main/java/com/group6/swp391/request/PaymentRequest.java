package com.group6.swp391.request;

import com.group6.swp391.model.Order;
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
//    Order order;
//    Date paymentDate;
//    double paymentAmount;
//    double totalAmount;
//    double remainingAmount;
    private String orderId;
}
