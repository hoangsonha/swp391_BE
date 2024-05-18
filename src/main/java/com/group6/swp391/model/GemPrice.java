package com.group6.swp391.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GemPrice {
    private int gemPriceID;
    private String origin;
    private int weightID;
    private int colorID;
    private int clarifyID;
    private int cutID;
    private double price;
}
