package com.group6.swp391.repository;

import com.group6.swp391.model.CollectionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionProductRepository extends JpaRepository<CollectionProduct, Integer> {
}
