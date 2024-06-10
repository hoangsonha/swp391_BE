package com.group6.swp391.repository;

import com.group6.swp391.model.ProductCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCustomRepository extends JpaRepository<ProductCustom, String> {
    public ProductCustom getProductCustomByProdcutCustomId(String id);
}
