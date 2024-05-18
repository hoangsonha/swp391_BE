package com.group6.swp391.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Points {
    private int pointsID;
    private int customerID;
    private int point;
    private int orderID;
}
