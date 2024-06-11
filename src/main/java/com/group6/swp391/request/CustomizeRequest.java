package com.group6.swp391.request;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class CustomizeRequest {
    private String productId;
    private String diamondId;
    private int size;
    private double totalPrice;

    public CustomizeRequest(String productId, String diamondId, int size, double totalPrice) {
        this.productId = productId;
        this.diamondId = diamondId;
        this.size = size;
        this.totalPrice = totalPrice;
    }
}
