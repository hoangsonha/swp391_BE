package com.group6.swp391.repository;

import com.group6.swp391.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    public Product getProductByName (String productName);
}
