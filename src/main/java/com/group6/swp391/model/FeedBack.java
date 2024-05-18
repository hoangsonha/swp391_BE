package com.group6.swp391.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedBack {
    private int feedBackID;
    private String comment;
    private double rating;
    private int orderID;
}
