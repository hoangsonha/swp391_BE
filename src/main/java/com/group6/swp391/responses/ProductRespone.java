package com.group6.swp391.responses;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class ProductRespone {
    private String productId;
    private String productName;
    private double price;
    private String thumnail;
}
