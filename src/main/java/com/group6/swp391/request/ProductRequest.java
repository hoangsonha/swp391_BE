package com.group6.swp391.request;

import com.group6.swp391.model.Category;
import com.group6.swp391.model.ProductSize;
import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String productID;
    private String productName;
    private String bathStone;
    private String brand;
    private String goleType;
    private double goldWeight;
    private String cutDiamond;
    private double dimensionsDiamond;
    private String oldGold;
    private String productType;
    private int quantity;
    private int quantityStonesOfDiamond;
    private double totalPrice;
    private double rating;
    private boolean status;
    private double originalPrice;
    private double ratio;
    private Category category;
    private List<ThumbnailRequest> imageUrls;
    private List<SizeRequest> sizes;
}
