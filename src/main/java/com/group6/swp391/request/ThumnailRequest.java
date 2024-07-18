package com.group6.swp391.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThumnailRequest {

    int imageId;

    @NotBlank(message = "URL hình ảnh không được để trống")
    String imageUrl;
}
