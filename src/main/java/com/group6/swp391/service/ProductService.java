package com.group6.swp391.service;

import com.group6.swp391.model.Product;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    public Product getProductByName(String productName);
}
