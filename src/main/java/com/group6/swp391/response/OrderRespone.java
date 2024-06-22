package com.group6.swp391.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.OrderDetail;
import com.group6.swp391.model.ProductCustomize;
import lombok.*;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRespone {
    private int orderId;
    @JsonIgnore
    private OrderDetail orderDetail;

    public int getQuantity() {
        return orderDetail != null ? orderDetail.getQuantity() : 0;
    }
    public double getPrice() {
        return orderDetail != null ? orderDetail.getPrice() : 0.0;
    }


    public Diamond getDiamond() {
        return orderDetail != null ? orderDetail.getDiamond() : null;
    }

    public ProductCustomize getProductCustomize() {
        return orderDetail != null ? orderDetail.getProductCustomize() : null;
    }


}
