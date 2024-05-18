package com.group6.swp391.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diamond {
    private int diamondID;
    private String diamondName;
    private int warrantyCardID;
    private double carat;
    private int sizeID;
    private String brand;
    private int certificateNumber;
    private int gemPriceID;
    private String image;
    private int flourescenceID;
}
