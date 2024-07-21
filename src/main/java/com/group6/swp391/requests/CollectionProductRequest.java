package com.group6.swp391.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionProductRequest {
    @NotNull(message = "Vui lòng nhập mã sản phẩm bộ sưu tập")
    int collectionProductId;

    @NotBlank(message = "Mã sản phẩm không được để trống")
    @NotNull(message = "Vui lòng nhập mã sản phẩm")
    String productId;
}
