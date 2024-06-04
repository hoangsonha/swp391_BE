package com.group6.swp391.service;

import com.group6.swp391.model.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(String productId);
}
