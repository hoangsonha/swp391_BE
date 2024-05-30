package com.group6.swp391.service;

import com.group6.swp391.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    Category createCategory(Category category);
    Category getCategoryById(int id);
    List<Category> getCategoryByNmae(String categoryName);
    List<Category> getAll();
    void  updateCategory(Category category);
    void deleteCategory(int id);
    Category getCategoryByName(String name);
}
