package com.group6.swp391.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThumnailRequest {

    int imageId;

    @NotBlank(message = "URL hình ảnh không được để trống")
    String imageUrl;
}
