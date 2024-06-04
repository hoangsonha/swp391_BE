package com.group6.swp391.repository;

import com.group6.swp391.model.Payment;
import com.group6.swp391.model.ProductCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCustomeRepository extends JpaRepository<ProductCustom, Integer> {
}
