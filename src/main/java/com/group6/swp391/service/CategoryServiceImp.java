package com.group6.swp391.service;

import com.group6.swp391.model.Category;
import com.group6.swp391.repository.CategoryRepository;
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
    public List<Category> getCategoryByNmae(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByCategoryName(name);
    }
}
