package com.group6.swp391.service;

import com.group6.swp391.model.Product;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ProductService {
    void createProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(String productId);
    void deleteProductStatus(String productId);
    void updateProduct(String id, Product product);
    List<Product> getProductsByCategory(String categoryName);
    List<Product> getProductByCondition(String shape, float dimension);
    void deleteProducts(List<String> productIds);
}
