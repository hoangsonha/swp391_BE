package com.group6.swp391.service;

import com.group6.swp391.model.Product;
import com.group6.swp391.model.ProductImage;

import java.util.List;

public interface ProductImageService {
    List<ProductImage> createProductImage(List<ProductImage> productImages);
    ProductImage updateProductImage(ProductImage productImage);
    List<ProductImage> getProductImageByProductId(String id);
}
