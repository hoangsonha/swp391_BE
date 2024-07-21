package com.group6.swp391.responses;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WarrantyCardDetailRespone {
    private int userId;
    private String fullName;
    private String address;
    private String email;
    private String phone;
    private int orderId;
    private Date purchaseDate;
    private Date expirationDate;
    private String objectId;
    private String objectType;
    private double price;
}
