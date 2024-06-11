package com.group6.swp391.repository;

import com.group6.swp391.model.ProductCustomize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCustomizeRepository extends JpaRepository<ProductCustomize, String> {
    @Query("SELECT ps FROM ProductCustomize ps WHERE  ps.prodcutCustomId LIKE :productcustomize")
    ProductCustomize getProductCustomByProdcutCustomId(@Param("productcustomize") String id);
}
