package com.group6.swp391.services;

import com.group6.swp391.pojos.Category;
import com.group6.swp391.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService{

    @Autowired private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        if(category == null) {
            throw new NullPointerException("category is null");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findByCategoryID(id);
    }

    @Override
    public List<Category> GetAllWithName(String categoryName) {
        return categoryRepository.findAllWithName(categoryName);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

    @Override
    public Category updateCategory(int categoryId, Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }
}
