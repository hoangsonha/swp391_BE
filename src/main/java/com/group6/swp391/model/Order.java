package com.group6.swp391.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int orderID;
    private double prepaidAmount;
    private int customerID;
    private int quantity;
    private double price;
    private String status;
}
