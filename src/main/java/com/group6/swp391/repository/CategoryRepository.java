package com.group6.swp391.repository;

import com.group6.swp391.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.categoryName LIKE %:categoryName")
    List<Category> findByName(@Param("categoryName") String categoryName);

    Category findByCategoryID(int id);
}
