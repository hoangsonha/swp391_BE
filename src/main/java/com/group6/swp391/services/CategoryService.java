package com.group6.swp391.services;

import com.group6.swp391.pojos.Category;

import java.util.List;


public interface CategoryService {
    Category createCategory(Category category);

    Category getCategoryById(int id);

    List<Category> GetAllWithName(String categoryName);

    List<Category> getAll();

    Category getByName(String categoryName);

    Category updateCategory(int categoryId, Category category);

    void deleteCategory(int id);
}
