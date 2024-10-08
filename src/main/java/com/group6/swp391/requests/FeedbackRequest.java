package com.group6.swp391.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackRequest {
    String comment;

    double rating;

    String productID;

    @NotNull(message = "Mã người dùng không được để trống")
    int userID;
}
