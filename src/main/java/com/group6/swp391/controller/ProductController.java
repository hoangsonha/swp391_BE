package com.group6.swp391.controller;

import com.group6.swp391.model.*;
import com.group6.swp391.response.ProductResponse;
import com.group6.swp391.response.SizeRespone;
import com.group6.swp391.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/swp391/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    ProductServiceImp productServiceImp;
    @Autowired
    SizeServiceImp sizeServiceImp;
    @Autowired
    ProductSizeServiceImp productSizeServiceImp;
    @Autowired
    ThumbnailSericeImp thumbnailSericeImp;
    @Autowired CategoryServiceImp categoryServiceImp;

    @Autowired DiamondServiceImp diamondServiceImp;

    @PostMapping("/create_product")
    public ResponseEntity<?> createProduct(@RequestBody ProductResponse productResponse) {
        try {
            Product product = productResponse.getProduct();
            List<SizeRespone> sizeRespones = productResponse.getSizes();
            List<Thumnail> thumnails = productResponse.getThumnails();
            if(productResponse == null) {
                throw new RuntimeException("Product response is null");
            } else {
                if(product == null) {
                    throw new RuntimeException("Product is null");
                } else {
                    if(productServiceImp.getProductById(product.getProductID()) != null) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Product already exists");
                    } else {
                        Category existingCategory = categoryServiceImp.getByName(product.getCategory().getCategoryName());
                        if(existingCategory == null) {
                            throw new RuntimeException("Category does not exist");
                        } else {
                            product.setCategory(existingCategory);
                        }
                        product.setStatus(true);
                        product.setTotalPrice(product.getOriginalPrice() * (1 + product.getRatio()));
                        productServiceImp.createProduct(product);
                        // xu li size

                        List<ProductSize> productSizes = new ArrayList<>();
                        for (SizeRespone sizeRespone : sizeRespones) {
                            Size size =sizeServiceImp.getSizeByValue(sizeRespone.getSizeValue());
                            if(size == null) {
                                size = new Size();
                                size.setSizeValue(sizeRespone.getSizeValue());
                                sizeServiceImp.createSize(size);
                            } else {
                                size.setSizeValue(sizeRespone.getSizeValue());
                            }
                                ProductSize productSize = new ProductSize();
                                productSize.setQuantiy(sizeRespone.getQuantitySize());
                                productSize.setProduct(product);
                                productSize.setSize(size);
                                productSizes.add(productSize);
                                productSizeServiceImp.createProductSize(productSize);
                        }
                        product.setProductSizes(productSizes);
                        // xu li hinh anh
                        List<Thumnail> setThumnails = new ArrayList<>();
                        for (Thumnail item : thumnails) {
                            Thumnail thumnail = new Thumnail();
                            thumnail.setImageUrl(item.getImageUrl());
                            thumnail.setProduct(product);
                            setThumnails.add(thumnail);
                            thumbnailSericeImp.createThumbnail(thumnail);
                        }
                        product.setProductImages(setThumnails);
                    }
                }
                return ResponseEntity.status(HttpStatus.CREATED).body("create product success");
            }
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all_products")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productServiceImp.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

//    @GetMapping("/product/{product_id}")
//    public ResponseEntity<List<Diamond>> getProductById(@PathVariable("product_id") String productId) {
//        Product product = productServiceImp.getProductById(productId);
//        List<Diamond> diamonds = diamondServiceImp.getByCondition(product.getShapeDiamond(), product.getDimensionsDiamond());
//        return new ResponseEntity<>(diamonds, HttpStatus.OK);
//    }
}
