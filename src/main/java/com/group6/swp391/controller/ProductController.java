package com.group6.swp391.controller;

import com.group6.swp391.model.*;
import com.group6.swp391.request.ProductRequest;
import com.group6.swp391.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /**
     * Method tao new product
     * @param productRequest productRequest
     * @return message success or fail
     */
    @PreAuthorize("hasRole('ADMIN')")
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


    /**
     * Method get all product
     * @return list product
     */
    @GetMapping("/all_products")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productServiceImp.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Method tim kim product by id
     * @param productID productId
     * @return product
     */
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

    /**
     * Method tim kim product dua tren category name
     * @param  category_name
     * @return list product
     */
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

    /**
     * Method update product
     * @param id productId
     * @return message success or fail
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{product_id}")
    public ResponseEntity<?> updateProduct(@PathVariable("product_id") String id,@RequestBody Product product) {
        try {
            Product existingProduct = productServiceImp.getProductById(id);
            if(existingProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product not found");
            }
            if(product.getProductName() != null) {
                existingProduct.setProductName(product.getProductName());
            }
            if(product.getBathStone() != null) {
                existingProduct.setBathStone(product.getBathStone());
            }
            if(product.getBrand() != null) {
                existingProduct.setBrand(product.getBrand());
            }
            if(product.getGoldType() != null) {
                existingProduct.setGoldType(product.getGoldType());
            }
            if(product.getGoldWeight() > 0.0) {
                existingProduct.setGoldWeight(product.getGoldWeight());
            }
            if(product.getShapeDiamond() != null) {
                existingProduct.setShapeDiamond(product.getShapeDiamond());
            }
            if(product.getDimensionsDiamond() > 0.0) {
                existingProduct.setDimensionsDiamond(product.getDimensionsDiamond());
            }
            if(product.getMessage() != null) {
                existingProduct.setMessage(product.getMessage());
            }
            if(product.getOldGold() != null) {
                existingProduct.setOldGold(product.getOldGold());
            }
            if(product.getProductType() != null) {
                existingProduct.setProductType(product.getProductType());
            }
            if(product.getQuantity() > 0) {
                existingProduct.setQuantity(product.getQuantity());
            }
            if(product.getQuantityStonesOfDiamond() > 0 ) {
                existingProduct.setQuantityStonesOfDiamond(product.getQuantityStonesOfDiamond());
            }
            if(product.getOriginalPrice() > 0 && product.getWagePrice() > 0 && product.getRatio() > 0) {
                existingProduct.setOriginalPrice(product.getOriginalPrice());
                existingProduct.setWagePrice(product.getWagePrice());
                existingProduct.setRatio(product.getRatio());
                existingProduct.setTotalPrice((product.getWagePrice() + product.getOriginalPrice()) * (1 + product.getRatio()));
            } else if(product.getOriginalPrice() > 0 && product.getWagePrice() > 0 && product.getRatio() <= 0) {
                existingProduct.setOriginalPrice(product.getOriginalPrice());
                existingProduct.setWagePrice(product.getWagePrice());
                existingProduct.setTotalPrice((product.getOriginalPrice() + product.getWagePrice()) * (1 + existingProduct.getRatio()));
            } else if(product.getOriginalPrice() > 0 && product.getWagePrice() <= 0 && product.getRatio() <= 0) {
                existingProduct.setOriginalPrice(product.getOriginalPrice());
                existingProduct.setTotalPrice((product.getOriginalPrice() + existingProduct.getWagePrice()) * (1 + existingProduct.getRatio()));
            } else if (product.getOriginalPrice() > 0 && product.getWagePrice() <= 0 && product.getRatio() > 0){
                existingProduct.setOriginalPrice(product.getOriginalPrice());
                existingProduct.setRatio(product.getRatio());
                existingProduct.setTotalPrice((product.getOriginalPrice() + existingProduct.getWagePrice()) * (1 + product.getRatio()));
            } else if(product.getOriginalPrice() <= 0 && product.getWagePrice() > 0 && product.getRatio() > 0) {
                existingProduct.setWagePrice(product.getWagePrice());
                existingProduct.setRatio(product.getRatio());
                existingProduct.setTotalPrice((existingProduct.getOriginalPrice() + product.getWagePrice()) * (1 + product.getRatio()));
            } else if(product.getOriginalPrice() <= 0 && product.getWagePrice() <= 0 && product.getRatio() > 0) {
                existingProduct.setRatio(product.getRatio());
                existingProduct.setTotalPrice((existingProduct.getOriginalPrice() + existingProduct.getWagePrice()) * (1 + product.getRatio()));
            } else if(product.getOriginalPrice() <= 0 && product.getWagePrice() > 0 && product.getRatio() <= 0) {
                existingProduct.setWagePrice(product.getWagePrice());
                existingProduct.setTotalPrice((existingProduct.getOriginalPrice() + product.getWagePrice()) * (1 + existingProduct.getRatio()));
            }
            if(product.getSizes() != null) {
                updateProductSizes(existingProduct, product.getSizes());
            }
            if (product.getProductImages() != null) {
                updateProductThumbnails(existingProduct, product.getProductImages());
            }
            productServiceImp.updateProduct(id, existingProduct);
            return ResponseEntity.ok().body("Product updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Method xoa product bang cach thay doi trang thai
     * true => false
     * @return message success or fail
     */
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * Method xoa product bang cach thay doi trang thai
     * true => false
     * thuc hien tren nhiu doi tuong
     * @return message success or fail
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete_products")
    public ResponseEntity<String> deleteProducts(@RequestBody List<String> prodcutIds) {
        try {
            if(prodcutIds == null || prodcutIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            for (int i = 0; i < prodcutIds.size(); i++) {
                String productID = prodcutIds.get(i);
                Product product = productServiceImp.getProductById(productID);
                if(product == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                } else {
                    product.setStatus(false);
                }
            }
            productServiceImp.deleteProducts(prodcutIds);
            return ResponseEntity.ok().body("delete products successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * method update size cuar product
     * @param newSizes product, list size
     * @return message success or fail
     */
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

    /**
     * method update image cuar product
     * @return message success or fail
     */
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
