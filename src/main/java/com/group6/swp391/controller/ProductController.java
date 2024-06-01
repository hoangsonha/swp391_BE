package com.group6.swp391.controller;

import com.group6.swp391.model.Category;
import com.group6.swp391.model.ChangePrice;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.ProductImage;
import com.group6.swp391.request.ProductRequest;
import com.group6.swp391.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/swp391/api/products")
public class ProductController {

    @Autowired
    ChangePriceService changePriceService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductIamgeServiceImp productImageService;

    @PostMapping("/create_product")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        ChangePrice changePrice = productRequest.getChangePrice();
        Product product = productRequest.getProduct();
        List<ProductImage> productImages = productRequest.getProductImages();
        ChangePrice newPrice;
        Product newProduct;
        try {
                changePrice.setTotalPrice((changePrice.getPriceDiamond() + changePrice.getWage() + changePrice.getSheathDiamond())*(1+ changePrice.getRatio()));
                newPrice = changePriceService.createPrice(changePrice);

            if (productService.getProductByProductID(product.getProductID()) != null) {
                throw new RuntimeException("Product already exist");
            } else {
                Category existCategory = categoryService.getByName(product.getCategory().getCategoryName());
                if (existCategory != null) {
                    product.setCategory(existCategory);
                } else {
                    throw new RuntimeException("Category does not exist");
                }

                product.setChangePrice(newPrice);
                if(product.getDiamond() == null) {
                    newPrice.setPriceDiamond(0);
                } else {
                    newPrice.setSheathDiamond(product.getDiamond().getTotalPrice());
                }
                changePriceService.updatePrice(newPrice.getChangePriceID(), newPrice);

                product.setTotalPrice(newPrice.getTotalPrice());
                product.setStatus(true);
                newProduct = productService.creatPrdoduct(product);
                return  ResponseEntity.ok("created product with ID: " + newProduct.getProductID());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
