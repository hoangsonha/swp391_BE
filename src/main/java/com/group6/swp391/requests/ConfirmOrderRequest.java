package com.group6.swp391.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfirmOrderRequest {

    @NotNull(message = "Vui lòng nhập trạng thái")
    @NotBlank(message = "Trạng thái không được để trống")
    @Size(max = 20, message = "Trạng thái phải từ 6 tới 100 kí tự")
    String status;

    @NotNull(message = "Vui lòng nhập lí do")
    @NotBlank(message = "Lí do không được để trống")
    @Size(max = 300, message = "Lí do phải nhỏ hơn 300 kí tự")
    String reason;
}
