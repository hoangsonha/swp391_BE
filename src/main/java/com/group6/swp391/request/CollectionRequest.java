package com.group6.swp391.request;

import com.group6.swp391.model.Thumnail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionRequest {
    @NotBlank(message = "Tiêu đề bộ sưu tập không được để trống")
    String collectionTitle;

    @NotBlank(message = "Tên bộ sưu tập không được để trống")
    String collectionName;

    @NotBlank(message = "Loại đá quý không được để trống")
    String gemStone;

    @NotBlank(message = "Loại vàng không được để trống")
    String goldType;

    @NotBlank(message = "Tuổi vàng không được để trống")
    String goldOld;

    String status;

    @Valid
    @NotNull(message = "Danh sách ảnh không được để trống")
    @Size(min = 1, message = "Danh sách ảnh phải có ít nhất 1 ảnh")
    List<Thumnail> thumnails;

    @Valid
    @NotNull(message = "Danh sách sản phẩm bộ sưu tập không được để trống")
    @Size(min = 1, message = "Danh sách sản phẩm bộ sưu tập phải có ít nhất 1 sản phẩm")
    List<CollectionProductRequest> collectionProductRequests;
}
