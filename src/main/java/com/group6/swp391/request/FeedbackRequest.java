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
    @NotBlank(message = "Bình luận không được để trống")
    String comment;

    @Min(value = 0, message = "Đánh giá phải lớn hơn hoặc bằng 0")
    @Max(value = 5, message = "Đánh giá phải nhỏ hơn hoặc bằng 5")
    @NotNull(message = "Đánh giá không được để trống")
    double rating;

    String diamondID;

    String productID;

    String collectionID;

    @NotNull(message = "Mã người dùng không được để trống")
    int userID;
}
