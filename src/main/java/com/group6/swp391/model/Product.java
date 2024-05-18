package com.group6.swp391.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private int productID;
    private String productName;
    private int categoryID;
    private double price;
    private int sizeID;
    private String brand;
    private int weightID;
    private String image;
    private int diamondID;
    private int quantityStonesOfDiamond;
    private double wage;
    private double totalPrice;
}
