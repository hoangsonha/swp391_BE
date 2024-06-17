package com.group6.swp391.request;

import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WarrantyCardRequest {
    private int userId;
    List<String> objectId;
    private Date expirationDate;
}
