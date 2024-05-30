package com.group6.swp391.repository;

import com.group6.swp391.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    public Product getProductsByProductName(String productName);

    public Product getProductsByProductID(String productId);
}
