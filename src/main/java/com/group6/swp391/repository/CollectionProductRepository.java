package com.group6.swp391.repository;

import com.group6.swp391.model.CollectionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionProductRepository extends JpaRepository<CollectionProduct, Integer> {
    @Query("SELECT cp FROM CollectionProduct cp WHERE cp.id =:id")
    CollectionProduct findColectionProductId(@Param("id") int id);
}
