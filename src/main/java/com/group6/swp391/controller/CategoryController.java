package com.group6.swp391.controller;

import com.group6.swp391.model.Category;
import com.group6.swp391.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swp391/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/create_category")
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        try {
            if (categoryService.getCategoryById(category.getCategoryID()) != null) {
                return ResponseEntity.badRequest().body("Category existed");
            } else {
                Category newCategory = categoryService.createCategory(category);
                return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully with ID: " + newCategory.getCategoryID());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }

    @GetMapping("/all_categories")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categoryList;
        try {
            categoryList = categoryService.getAll();
            if (categoryList == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No Category found");
            } else {
                return ResponseEntity.ok(categoryList);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/categoryId/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable("categoryId") int categoryId) {
        Category findCategory = categoryService.getCategoryById(categoryId);
        if(findCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        } else {
            return ResponseEntity.ok(findCategory);
        }
    }

    @GetMapping("/categoryAllName/{categoryName}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String categoryName) {
        List<Category> categoryList;
        try {
            if(categoryName == null) {
                categoryList = categoryService.getAll();
            } else {
                categoryList = categoryService.GetAllWithName(categoryName);
                if(categoryList == null) {}
            }
            return ResponseEntity.ok(categoryList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/categoryName/{categoryName}")
    public ResponseEntity<Category> getByName(@PathVariable String categoryName) {
        Category existCategory;
        try {
            if(categoryName == null) {
                throw new RuntimeException("Category name not null");
            } else {
                existCategory = categoryService.getByName(categoryName);
                if(existCategory == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                } else {
                    return ResponseEntity.ok(existCategory);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("update_category/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable("categoryId") int id,
                                            @RequestBody Category category) {
        try {
            Category findCategory = categoryService.getCategoryById(id);
            if(findCategory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
            } else {
                findCategory.setCategoryName(category.getCategoryName());
                findCategory.setUpdateAt(category.getUpdateAt());
            }
            categoryService.updateCategory(id, findCategory);
            return ResponseEntity.ok("Category updated successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
