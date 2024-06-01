package com.group6.swp391.service;

import com.group6.swp391.model.Product;
import com.group6.swp391.model.ProductImage;
import com.group6.swp391.repository.ProductImageRepository;
import com.group6.swp391.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductIamgeServiceImp implements ProductImageService {
    @Autowired
    ProductImageRepository productImageRepository;


    @Override
    public List<ProductImage> createProductImage(List<ProductImage> productImages) {
        return productImageRepository.saveAll(productImages);
    }

    @Override
    public ProductImage updateProductImage(ProductImage productImage) {
        return null;
    }

    @Override
    public List<ProductImage> getProductImageByProductId(String id) {
        return productImageRepository.findByProductId(id);
    }


}
