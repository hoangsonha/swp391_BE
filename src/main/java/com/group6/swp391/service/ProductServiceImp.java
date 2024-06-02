package com.group6.swp391.service;

import com.group6.swp391.model.Product;
import com.group6.swp391.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired private ProductRepository productRepository;

    @Override
    public Product getProductByProductName(String productName) {
        return productRepository.getProductsByProductName(productName);
    }

    @Override
    public Product getProductByProductID(String productID) {
        return productRepository.getProductsByProductID(productID);
    }

    @Override
    public Product creatProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String productID) {
        productRepository.deleteById(productID);
    }

    @Override
    public void markProductAsDeleted(String productID) {
        Product product = getProductByProductID(productID);
        if (product != null) {
            product.setStatus(false);
            productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found with id: " + productID);
        }
    }
}
