package com.group6.swp391.service;

import com.group6.swp391.model.Product;
import com.group6.swp391.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp  implements  ProductService{
    @Autowired
    ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return  productRepository.findAll();
    }

    @Override
    public Product getProductById(String productId) {
        return productRepository.findProductId(productId);
    }

    @Override
    public void deleteProductStatus(String productId) {
        try {
            Product product = productRepository.findProductId(productId);
            if(product == null) {
                throw new RuntimeException("Product not found");
            } else {
                product.setStatus(false);
                productRepository.save(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
