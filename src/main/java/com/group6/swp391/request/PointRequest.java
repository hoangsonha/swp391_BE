package com.group6.swp391.request;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointRequest {
    private int userId;
    private int orderId;
    private double usedPoint;
}
