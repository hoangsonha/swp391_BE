package com.group6.swp391.service;

import com.group6.swp391.model.Product;

public interface ProductService {

    Product getProductByProductName(String productName);

    Product getProductByProductID(String productID);

    Product creatPrdoduct(Product product);
}
