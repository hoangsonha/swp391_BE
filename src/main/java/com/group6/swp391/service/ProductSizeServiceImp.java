package com.group6.swp391.service;

import com.group6.swp391.model.ProductSize;
import com.group6.swp391.repository.ProductSizeReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSizeServiceImp implements ProductSizeService {
    @Autowired
    ProductSizeReposity productSizeReposity;

    @Override
    public ProductSize createProductSize(ProductSize productSize) {
        return productSizeReposity.save(productSize);
    }

    @Override
    public List<ProductSize> getAllProductSizes() {
        return productSizeReposity.findAll();
    }

    @Override
    public List<ProductSize> getByProduct(String productId) {
        return productSizeReposity.findByProduct(productId);
    }

    @Override
    public List<ProductSize> getBySize(int sizeId) {
        return productSizeReposity.findBySize(sizeId);
    }

    @Override
    public void UpdateProductSize(String productId, int sizeId, ProductSize productSize) {
        ProductSize existingProductSize = productSizeReposity.getByPS(productId, sizeId);
        if(existingProductSize != null) {
            existingProductSize.setProduct(productSize.getProduct());
            existingProductSize.setSize(productSize.getSize());
            existingProductSize.setQuantiy(productSize.getQuantiy());
            productSizeReposity.save(existingProductSize);
        }
    }

    @Override
    public void DeleteProductSize(String productId, int sizeId) {

    }
}
