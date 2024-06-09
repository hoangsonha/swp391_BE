package com.group6.swp391.controller;

import com.group6.swp391.model.*;
import com.group6.swp391.request.ProductRequest;
import com.group6.swp391.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/swp391/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired ProductServiceImp productServiceImp;
    @Autowired CategoryServiceImp categoryServiceImp;
    @Autowired DiamondServiceImp diamondServiceImp;
    @Autowired SizeServiceImp sizeServiceImp;
    @Autowired ThumnailServiceImp thumnailServiceImp;

    @PostMapping("create_product")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            Product product = productRequest.getProduct();
            if(productServiceImp.getProductById(product.getProductID()) != null) {
                return ResponseEntity.badRequest().body("Product already exists");
            } else {
                product.setStatus(true);
                product.setTotalPrice((product.getWagePrice() + product.getOriginalPrice())*(1+ product.getRatio()));
                Category existingCate = categoryServiceImp.getByName(product.getCategory().getCategoryName());
                if(existingCate == null) {
                    throw new RuntimeException("Category not found");
                } else {
                    product.setCategory(existingCate);
                }
                productServiceImp.createProduct(product);
                List<Size> setSize = new ArrayList<>();
                for (Size size : productRequest.getSizes()) {
                    Size newSize = new Size();
                    newSize.setSizeValue(size.getSizeValue());
                    newSize.setQuantity(size.getQuantity());
                    newSize.setProduct(product);
                    setSize.add(newSize);
                    sizeServiceImp.createSize(newSize);
                }
                List<Thumnail> setThumnail = new ArrayList<>();
                for (Thumnail th : productRequest.getProductImages()) {
                    Thumnail newThumnail = new Thumnail();
                    newThumnail.setImageUrl(th.getImageUrl());
                    newThumnail.setProduct(product);
                    setThumnail.add(newThumnail);
                    thumnailServiceImp.createThumnail(newThumnail);
                }

                product.setProductImages(setThumnail);
                product.setSizes(setSize);
            }
            return ResponseEntity.ok().body("Product created successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/all_products")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productServiceImp.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/product/{product_id}")
    public ResponseEntity<Product> getProductById(@PathVariable("product_id") String productID) {
        try {
            Product product = productServiceImp.getProductById(productID);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(product);
        }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/category/{category_name}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable("category_name") String category_name) {
        try {
            List<Product> products = productServiceImp.getProductsByCategory(category_name);
            if(products == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("update/{product_id}")
    public ResponseEntity<?> updateProduct(@PathVariable("product_id") String id,@RequestBody Product product) {
        try {
            Product existingProduct = productServiceImp.getProductById(id);
            if(existingProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product not found");
            }
            existingProduct.setProductID(product.getProductID());
            existingProduct.setProductName(product.getProductName());
            existingProduct.setBathStone(product.getBathStone());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setGoldType(product.getGoldType());
            existingProduct.setGoldWeight(product.getGoldWeight());
            existingProduct.setShapeDiamond(product.getShapeDiamond());
            existingProduct.setDimensionsDiamond(product.getDimensionsDiamond());
            existingProduct.setMessage(product.getMessage());
            existingProduct.setOldGold(product.getOldGold());
            existingProduct.setProductType(product.getProductType());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct.setQuantityStonesOfDiamond(product.getQuantityStonesOfDiamond());
            existingProduct.setTotalPrice((product.getWagePrice() + product.getOriginalPrice()) * (1 + product.getRatio()));
            existingProduct.setOriginalPrice(product.getOriginalPrice());
            existingProduct.setWagePrice(product.getWagePrice());
            existingProduct.setRatio(product.getRatio());
            Category existingCategory = categoryServiceImp.getByName(product.getCategory().getCategoryName());
            if(existingCategory == null) {
                throw new RuntimeException("Category not found");
            }
            updateProductSizes(existingProduct, product.getSizes());
            updateProductThumbnails(existingProduct, product.getProductImages());
            productServiceImp.updateProduct(id, existingProduct);
            return ResponseEntity.ok().body("Product updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{product_id}")
    public ResponseEntity<String> deleteProductStatus(@PathVariable("product_id") String productID) {
        try {
            Product product = productServiceImp.getProductById(productID);
            if(product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                productServiceImp.deleteProductStatus(product.getProductID());
                return ResponseEntity.ok().body("delete product success with product ID: " + product.getProductID());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    private void updateProductSizes(Product existingProduct, List<Size> newSizes) {
        List<Size> currentSizes = existingProduct.getSizes();
        List<Size> sizesToRemove = new ArrayList<>(currentSizes);

        for (Size newSize : newSizes) {
            boolean found = false;
            for (Size existingSize : currentSizes) {
                if (existingSize.getSizeID() == newSize.getSizeID()) {
                    // Cập nhật thông tin cho size đã tồn tại
                    existingSize.setSizeValue(newSize.getSizeValue());
                    existingSize.setQuantity(newSize.getQuantity());
                    sizesToRemove.remove(existingSize);
                    found = true;
                    break;
                }
            }
            if (!found) {
                // Thêm size mới vào danh sách nếu không tìm thấy size cũ
                newSize.setProduct(existingProduct);
                currentSizes.add(newSize);
            }
        }

        // Xóa các sizes không còn liên kết
        currentSizes.removeAll(sizesToRemove);
        for (Size sizeToRemove : sizesToRemove) {
            sizeServiceImp.deleteSize(sizeToRemove.getSizeID());
        }
    }

    private void updateProductThumbnails(Product existingProduct, List<Thumnail> newThumbnails) {
        List<Thumnail> currentThumbnails = existingProduct.getProductImages();
        List<Thumnail> thumbnailsToRemove = new ArrayList<>(currentThumbnails);

        for (Thumnail newThumbnail : newThumbnails) {
            boolean found = false;
            for (Thumnail existingThumbnail : currentThumbnails) {
                if (existingThumbnail.getImageId() == newThumbnail.getImageId()) {
                    // Cập nhật URL cho thumbnail đã tồn tại
                    existingThumbnail.setImageUrl(newThumbnail.getImageUrl());
                    thumbnailsToRemove.remove(existingThumbnail);
                    found = true;
                    break;
                }
            }
            if (!found) {
                // Thêm thumbnail mới vào danh sách nếu không tìm thấy thumbnail cũ
                newThumbnail.setProduct(existingProduct);
                currentThumbnails.add(newThumbnail);
            }
        }

        // Xóa các thumbnails không còn liên kết
        currentThumbnails.removeAll(thumbnailsToRemove);
        for (Thumnail thumnailToRemove : thumbnailsToRemove) {
            thumnailServiceImp.deleteThumnail(thumnailToRemove.getImageId());
        }
    }

}
