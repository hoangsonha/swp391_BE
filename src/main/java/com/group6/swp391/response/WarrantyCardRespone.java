package com.group6.swp391.response;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WarrantyCardRespone {
    private int warrantyCardID;
    private String objectId;
    private Date purchaseDate;
    private Date expirationDate;

}
