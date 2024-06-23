package com.group6.swp391.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group6.swp391.model.OrderDetail;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListOrderUserResphone {
    private int orderId;
    private int quantity;
    private double discount;
    private double price;
    private String status;
    private LocalDateTime orderDate;
    private List<OrderDetail> orderDetail;

}
