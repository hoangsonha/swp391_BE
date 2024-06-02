package com.group6.swp391.service;

import com.group6.swp391.model.Product;

import java.util.List;

public interface ProductService {

    Product getProductByProductName(String productName);

    Product getProductByProductID(String productID);

    Product creatProduct(Product product);

    List<Product> getAllProduct();

    Product updateProduct(Product product);

    void deleteProduct(String productID);

    void markProductAsDeleted(String productID);
}
