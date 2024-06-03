package com.group6.swp391.controller;

import com.group6.swp391.model.Product;
import com.group6.swp391.request.ProductRequest;
import com.group6.swp391.service.ProductService;
import com.group6.swp391.service.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/swp391/api/products")
public class ProductController {
    @Autowired
    ProductServiceImp productServiceImp;

    @GetMapping("/all_products")
    public ResponseEntity<?> getAllProducts() {
        List<ProductRequest> products = productServiceImp.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
