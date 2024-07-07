package com.group6.swp391.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarrantyCardRequest {
    @NotNull(message = "ID người dùng không được để trống")
    int userId;

    @NotEmpty(message = "Danh sách ID đối tượng không được để trống")
    List<String> objectId;

    @NotNull(message = "Ngày hết hạn không được để trống")
    @Future(message = "Ngày hết hạn phải là một ngày trong tương lai")
    Date expirationDate;
}
