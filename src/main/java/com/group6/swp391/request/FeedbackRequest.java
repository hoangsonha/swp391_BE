package com.group6.swp391.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackRequest {
    private String comment;
    private double rating;
    private String diamondID;
    private String productID;
    private String collectionID;
    private int userID;
}
