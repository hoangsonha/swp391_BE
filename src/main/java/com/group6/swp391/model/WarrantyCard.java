package com.group6.swp391.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyCard {
    private int warrantyCardID;
    private Date purchaseDate;
    private Date expirationDate;
    private int userID;
    private int diamondID;
}
