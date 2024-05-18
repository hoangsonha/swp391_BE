package com.group6.swp391.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePrice {
    private int changePriceID;
    private double originPrice;
    private double ratio;
    private double wage;
}
