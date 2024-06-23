package com.group6.swp391.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group6.swp391.model.*;
import lombok.*;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRespone {

    @JsonIgnore
    private Order order;

    private int orderId;

    public double getDiscount() {
        return order != null ? order.getDiscount() : 0.0;
    }

    public double getPrice() {
        return order != null ? order.getPrice() : 0.0;
    }

    public int getQuantity() {
        return order != null ? order.getQuantity() : 0;
    }


    @JsonIgnore
    private OrderDetail orderDetail;

    public Diamond getDiamond() {
        return orderDetail != null ? orderDetail.getDiamond() : null;
    }

    public ProductCustomize getProductCustomize() {
        return orderDetail != null ? orderDetail.getProductCustomize() : null;
    }


}
