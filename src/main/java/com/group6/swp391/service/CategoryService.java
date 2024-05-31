package com.group6.swp391.service;

import com.group6.swp391.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    Category createCategory(Category category);
    Category getCategoryById(int id);
    List<Category> getCategoryByName(@Param("categoryName") String categoryName);
    List<Category> getAll();
    Category updateCategory(int categoryId, Category category);
    void deleteCategory(int id);
}
