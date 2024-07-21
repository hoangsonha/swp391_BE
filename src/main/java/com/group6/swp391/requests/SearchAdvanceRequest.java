package com.group6.swp391.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchAdvanceRequest {

    @NotNull(message = "Vui lòng nhập trọng lượng")
    private String carat;
    @NotNull(message = "Vui lòng nhập kích thước")
    private String size;
    @NotNull(message = "Vui lòng nhập độ cấp màu")
    private char color;
    @NotNull(message = "Vui lòng nhập độ tinh khiết")
    private String clarify;
    @NotNull(message = "Vui lòng nhập hình dạng")
    private String shape;
    @NotNull(message = "Vui lòng nhập giá")
    private String price;
    @NotNull(message = "Vui lòng chọn loại sắp xếp theo giá")
    private String optionPrice;
}
