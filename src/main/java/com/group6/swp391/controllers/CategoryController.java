package com.group6.swp391.controllers;

import com.group6.swp391.pojos.Category;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swp391/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    /**
     * Method create new category
     * @param category category
     * @return success or fail
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create_category")
    public ResponseEntity<ObjectResponse> createCategory(@RequestBody Category category) {
        try {
            if (categoryService.getCategoryById(category.getCategoryID()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed","Danh mục sản phẩm đã tồn tại", null));
            } else {
                Category newCategory = categoryService.createCategory(category);
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Tạo thành công", newCategory));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }


    /**
     * Method all category in data
     * @return List catrgory
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all_categories")
    public ResponseEntity<ObjectResponse> getAllCategories() {
        List<Category> categoryList = null;
        try {
            categoryList = categoryService.getAll();
            if (categoryList == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Danh sách danh mục sản phẩm rỗng", null));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Danh dách danh mục sản phẩm", categoryList));

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }


    /**
     * Method tìm kiem category by categoryId
     * @param categoryId categoryId
     * @return category
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/categoryId/{categoryId}")
    public ResponseEntity<ObjectResponse> getCategoryById(@PathVariable("categoryId") int categoryId) {
        try {
            Category findCategory = categoryService.getCategoryById(categoryId);
            if(findCategory == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Danh mục sản phẩm không tồn tại", null));

            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Danh mục sản phẩm", findCategory));

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }



    /**
     * Method tìm kiem category by categoryName
     * @param categoryName
     * @return List<category>
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/categoryAllName/{categoryName}")
    public ResponseEntity<ObjectResponse> getCategoryByName(@PathVariable String categoryName) {
        List<Category> categoryList;
        try {
            if(categoryName == null) {
                categoryList = categoryService.getAll();
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Failed","Vui lòng nhập tên danh mục sản phẩm", categoryList));
            } else {
                categoryList = categoryService.GetAllWithName(categoryName);
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Danh sách danh mục sản phẩm", categoryList));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }


    /**
     * Method tìm kiem category by categoryName
     * @param  categoryName
     * @return Category
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/categoryName/{categoryName}")
    public ResponseEntity<ObjectResponse> getByName(@PathVariable String categoryName) {
        Category existCategory;
        try {
            if(categoryName == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Vui lòng nhập tên danh mục sản phẩm", null));
            } else {
                existCategory = categoryService.getByName(categoryName);
                if(existCategory == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Danh mục sản phẩm không tồn tại tại với tên" + categoryName, null));
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Danh mục sản phẩm", existCategory));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }


    /**
     * Method update category
     * @param id categoryId and category
     * @return success or fail
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("update_category/{categoryId}")
    public ResponseEntity<ObjectResponse> updateCategory(@PathVariable("categoryId") int id,
                                                         @RequestBody Category category) {
        try {
            Category findCategory = categoryService.getCategoryById(id);
            if(findCategory == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Danh mục sản phẩm không tồn tại tại với id" + id, null));
            } else {
                findCategory.setCategoryName(category.getCategoryName());
                findCategory.setUpdateAt(category.getUpdateAt());
                categoryService.updateCategory(id, findCategory);
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Chỉnh sửa thành công", findCategory));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

}
