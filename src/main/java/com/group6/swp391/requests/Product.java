package com.group6.swp391.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @NotBlank(message = "ID sản phẩm không được trống")
    @NotNull(message = "Vui lòng nhập ID sản phẩm")
    private String productID;

    @NotBlank(message = "Tên sản phẩm không được bỏ trống")
    @NotNull(message = "Vui lòng nhập tên sản phẩm")
    private String productName;

    @NotBlank(message = "Loại đá tấm không được bỏ trống")
    @NotNull(message = "Vui lòng nhập loại đá tấm")
    private String bathStone;

    @NotBlank(message = "Thương hiệu không được bỏ trống")
    @NotNull(message = "Vui lòng nhập thương hiệu")
    private String brand;

    @NotBlank(message = "Loại vàng không được bỏ trống")
    @NotNull(message = "Vui lòng nhập loại vàng")
    private String goldType;

    @NotNull(message = "Vui lòng nhập trọng lượng vàng")
    @Min(value = 0, message = "Trọng lượng vàng phải lớn hơn 0")
    private float goldWeight;

    @NotNull(message = "Vui lòng nhập hình dạng kim cương")
    @NotBlank(message = "Hình dạng kim cương không được bỏ trống")
    private String shapeDiamond;

    @NotNull(message = "Vui lòng nhập kích thước đá chủ")
    @Min(value = 0, message = "Kích thước đá chủ phải lớn hơn 0")
    private float dimensionsDiamond;

    @NotNull(message = "Vui lòng nhập tuổi vàng")
    @NotBlank(message = "Tuổi vàng không được bỏ trống")
    private String oldGold;

    @NotNull(message = "Vui lòng nhập loại sản phẩm")
    @NotBlank(message = "Loại sản phẩm không được bỏ trống")
    private String productType;

    @NotNull(message = "Số lượng sản phẩm không được bỏ trống")
    @Min(value = 0, message = "Số lượng sản phẩm không được nhỏ hơn 0")
    private int quantity;

    @NotNull(message = "Số lượng đá tấm không được bỏ trống" )
    @Min(value = 0, message = "Số lượng đá tấm không được nhỏ hơn 0")
    private int quantityStonesOfDiamond;

    @NotNull(message = "Trọng lượng đá không được bỏ trống")
    @Min(value = 0, message = "Trọng lượng đá không được nhỏ hơn 0")
    private float stoneWeight;

    @NotNull(message = "Giá nhập không được bỏ trống")
    @Min(value = 0, message = "Giá nhập không được nhỏ hơn 0")
    private double originalPrice;

    @NotNull(message = "Tiền công không được bỏ trống")
    @Min(value = 0, message = "Tiền công không được nhỏ hơn 0")
    private double wagePrice;

    @NotNull(message = "Tỉ lệ áp giá không được bỏ trống")
    @Min(value = 0, message = "Tỉ lệ áp giá không được nhỏ hơn 0")
    private double ratio;
}
