package com.group6.swp391.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group6.swp391.model.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRespone {

    private int orderId;

    private double discount;

    private double price;

    private int quantity;

    private String status;

    private LocalDateTime orderDate;

    @JsonIgnore
    private OrderDetail orderDetail;

    public Diamond getDiamond() {
        return orderDetail != null ? orderDetail.getDiamond() : null;
    }

    public ProductCustomize getProductCustomize() {
        return orderDetail != null ? orderDetail.getProductCustomize() : null;
    }
}
