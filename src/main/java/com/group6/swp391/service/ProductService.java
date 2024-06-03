package com.group6.swp391.service;

import com.group6.swp391.model.Product;
import com.group6.swp391.request.ProductRequest;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    List<ProductRequest> getAllProducts();
    Product getProductById(String productId);
}
