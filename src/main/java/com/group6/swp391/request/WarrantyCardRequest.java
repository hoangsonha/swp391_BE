package com.group6.swp391.request;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WarrantyCardRequest {
    private int userId;
    private String productCustomizeId;
    private String diamondId;
    private Date expirationDate;
}
