package com.group6.swp391.requests;

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

    @Size(max = 20, min = 5, message = "Trọng lượng phải từ 5 tới 20 kí tự bao gồm cả @gmail.com")
    private String carat;

    @Size(max = 20, min = 5, message = "Kích thước phải từ 5 tới 20 kí tự bao gồm cả @gmail.com")
    private String size;

    @Size(max = 1, min = 1, message = "Cấp màu phải bao gồm 1 kí tự")
    private char color;

    @Size(max = 4, min = 2, message = "Độ tinh khiết phải từ 2 tới 4 kí tự")
    private String clarify;

    @Size(max = 7, min = 4, message = "Hình dạng phải từ 4 tới 7 kí tự bao gồm cả @gmail.com")
    private String shape;

    @Size(max = 30, min = 10, message = "Giá phải từ 10 tới 30 kí tự")
    private String price;

    @Size(max = 30, min = 10, message = "Sắp xếp giá phải từ 10 tới 30 kí tự")
    private String optionPrice;
}
