package com.group6.swp391.controller;

import com.group6.swp391.model.Category;
import com.group6.swp391.model.ChangePrice;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.ProductImage;
import com.group6.swp391.request.ProductRequest;
import com.group6.swp391.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            changePrice.setTotalPrice((changePrice.getPriceDiamond() + changePrice.getWage() + changePrice.getSheathDiamond()) * (1 + changePrice.getRatio()));
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
                if (product.getDiamond() == null) {
                    newPrice.setPriceDiamond(0);
                } else {
                    newPrice.setSheathDiamond(product.getDiamond().getTotalPrice());
                }
                changePriceService.updatePrice(newPrice.getChangePriceID(), newPrice);

                product.setTotalPrice(newPrice.getTotalPrice());
                product.setStatus(true);
                newProduct = productService.creatProduct(product);
                List<ProductImage> imagesTosave = new ArrayList<>();
                for (ProductImage productImage : productImages) {
                    productImage.setProduct(product);
                    imagesTosave.add(productImage);
                }
                product.setProductImages(imagesTosave);
                productImageService.createProductImage(imagesTosave);
                return ResponseEntity.ok("created product with ID: " + newProduct.getProductID());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/all_products")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products;
        try {
            products = productService.getAllProduct();
            if (products == null) {
                throw new RuntimeException("Product list is empty");
            } else {
                return ResponseEntity.ok(products);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get all products failed");
        }
    }

    @PutMapping("/update_product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") String id, @RequestBody ProductRequest productRequest) {
        try {
            Product existingProduct = productService.getProductByProductID(id);
            if (existingProduct == null) {
                return ResponseEntity.badRequest().body("Product not found with ID: " + id);
            }

            ChangePrice changePrice = productRequest.getChangePrice();
            Product product = productRequest.getProduct();
            Category category = productRequest.getCategory();
            List<ProductImage> productImages = productRequest.getProductImages();

            existingProduct.setProductName(product.getProductName());
            existingProduct.setBathStone(product.getBathStone());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setGoleType(product.getGoleType());
            existingProduct.setGoldWeight(product.getGoldWeight());
            existingProduct.setHostSize(product.getHostSize());
            existingProduct.setMessage(product.getMessage());
            existingProduct.setOldGold(product.getOldGold());
            existingProduct.setProductType(product.getProductType());
            existingProduct.setQuantityStonesOfDiamond(product.getQuantityStonesOfDiamond());

            Category existingCategory = categoryService.getByName(category.getCategoryName());
            if (existingCategory != null) {
                existingProduct.setCategory(existingCategory);
            } else {
                throw new RuntimeException("Category does not exist");
            }

            changePrice.setTotalPrice((changePrice.getPriceDiamond() + changePrice.getWage()
                    + changePrice.getSheathDiamond()) * (1 + changePrice.getRatio()));
            changePriceService.updatePrice(changePrice.getChangePriceID(), changePrice);

            existingProduct.setChangePrice(changePrice);
            existingProduct.setTotalPrice(changePrice.getTotalPrice());

            List<ProductImage> images = new ArrayList<>();
            for (ProductImage productImage : productImages) {
                productImage.setProduct(existingProduct);
                images.add(productImage);
            }
            existingProduct.setProductImages(images);
            productImageService.createProductImage(images);

            productService.updateProduct(existingProduct);

            return ResponseEntity.ok("Product updated successfully with ID: " + existingProduct.getProductID());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

//    @DeleteMapping("/delete_product/{id}")
//    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id) {
//        try{
//            productService.deleteProduct(id);
//            return ResponseEntity.ok("Deleted product with ID: " + id);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }

    @DeleteMapping("/delete_product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id) {
        try {
            productService.markProductAsDeleted(id);
            return ResponseEntity.ok("Deleted product with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
