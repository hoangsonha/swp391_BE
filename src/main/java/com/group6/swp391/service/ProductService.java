package com.group6.swp391.service;

import com.group6.swp391.model.Product;

public interface ProductService {

    public Product getProductByProductName(String productName);

    public Product getProductByProductID(String productID);
}
