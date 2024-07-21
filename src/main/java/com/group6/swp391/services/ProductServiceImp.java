package com.group6.swp391.services;

import com.group6.swp391.pojos.Product;
import com.group6.swp391.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp  implements  ProductService{
    @Autowired
    ProductRepository productRepository;

    @Override
    public void createProduct(Product product) {
        productRepository.save(product);
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

    @Override
    public void updateProduct(String id, Product product) {
        try {
            Product existingProduct = productRepository.findProductId(id);
            if(existingProduct == null) {
                throw new RuntimeException("Product not found");
            } else {
                productRepository.save(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategory(categoryName);
    }

    @Override
    public List<Product> getProductByCondition(String shape, float dimension) {
        return productRepository.getByCondition(shape, dimension);
    }

    @Override
    public void deleteProducts(List<String> productIds) {
        try {
            for (String id : productIds) {
                Product product = productRepository.findProductId(id);
                if(product == null) {
                    throw new RuntimeException("Product not found");
                } else {
                    productRepository.save(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
