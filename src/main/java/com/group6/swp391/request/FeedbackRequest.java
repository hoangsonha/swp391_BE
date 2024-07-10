package com.group6.swp391.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackRequest {
    String comment;

    double rating;

    String diamondID;

    String productID;

    String collectionID;

    @NotNull(message = "Mã người dùng không được để trống")
    int userID;
}
