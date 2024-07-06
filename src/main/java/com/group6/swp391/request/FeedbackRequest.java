package com.group6.swp391.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    String comment;

    @Min(0)
    @Max(5)
    @NotNull
    double rating;

    String diamondID;

    String productID;

    String collectionID;

    @NotNull
    int userID;
}
