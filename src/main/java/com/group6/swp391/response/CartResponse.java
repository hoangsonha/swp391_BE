package com.group6.swp391.response;

import com.group6.swp391.model.Diamond;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private String status;
    private String message;
    private Object cart;
    private List<Diamond> diamond;
    private int totalQuantity;
    private double totalPrice;
}
