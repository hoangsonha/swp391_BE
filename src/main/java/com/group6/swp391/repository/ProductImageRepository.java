package com.group6.swp391.repository;

import com.group6.swp391.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    @Query("SELECT pi FROM ProductImage  pi WHERE pi.product.productID =:productId")
    List<ProductImage> findByProductId(@Param("productId") String productId);
}
