package com.group6.swp391.service;

import com.group6.swp391.model.Product;
import com.group6.swp391.model.ProductImage;

import java.util.List;

public interface ProductImageService {
    ProductImage createProductImage(String id, ProductImage productImage);
    ProductImage updateProductImage(ProductImage productImage);
    List<ProductImage> getProductImageByProductId(String id);
}
