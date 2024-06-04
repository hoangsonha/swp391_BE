package com.group6.swp391.service;

import com.group6.swp391.model.ProductSize;

import java.util.List;

public interface ProductSizeService {
    ProductSize createProductSize(ProductSize productSize);
    List<ProductSize> getAllProductSizes();
    List<ProductSize> getByProduct(String productId);
    List<ProductSize> getBySize(int sizeId);
    void UpdateProductSize(String productId, int sizeId, ProductSize productSize);
    void DeleteProductSize(String productId, int sizeId);
}
