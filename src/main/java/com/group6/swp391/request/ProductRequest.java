package com.group6.swp391.request;

import com.group6.swp391.model.Category;
import com.group6.swp391.model.ChangePrice;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.ProductImage;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private Product product;
    private ChangePrice changePrice;
    private Category category;
    private List<ProductImage> productImages;
}
