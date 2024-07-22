package com.group6.swp391.controllers;

import com.group6.swp391.pojos.*;
import com.group6.swp391.requests.ProductRequest;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.services.*;
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

    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DiamondService diamondService;
    @Autowired
    SizeService sizeService;
    @Autowired
    ThumnailService thumnailService;

    /**
     * Method tao new product
     *
     * @param productRequest productRequest
     * @return message success or fail
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("create_product")
    public ResponseEntity<ObjectResponse> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            Product product = productRequest.getProduct();
            if (productService.getProductById(product.getProductID()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Sản phẩm đã tồn tại", null));
            } else {
                product.setStatus(true);
                product.setTotalPrice((product.getWagePrice() + product.getOriginalPrice()) * (1 + product.getRatio()));
                Category existingCate = categoryService.getByName(product.getCategory().getCategoryName());
                if (existingCate == null) {
                    throw new RuntimeException("Không tìm tấy loại sản phẩm");
                } else {
                    product.setCategory(existingCate);
                }
                productService.createProduct(product);
                List<Size> setSize = new ArrayList<>();
                for (Size size : productRequest.getSizes()) {
                    Size newSize = new Size();
                    newSize.setSizeValue(size.getSizeValue());
                    newSize.setQuantity(size.getQuantity());
                    newSize.setProduct(product);
                    setSize.add(newSize);
                    sizeService.createSize(newSize);
                }
                List<Thumnail> setThumnail = new ArrayList<>();
                for (Thumnail th : productRequest.getProductImages()) {
                    Thumnail newThumnail = new Thumnail();
                    newThumnail.setImageUrl(th.getImageUrl());
                    newThumnail.setProduct(product);
                    setThumnail.add(newThumnail);
                    thumnailService.createThumnail(newThumnail);
                }

                product.setProductImages(setThumnail);
                product.setSizes(setSize);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Tạo sản phẩm thành công", product));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Tạo sản phẩm thất bại", null));
        }
    }

    /**
     * Method tim kim product by id
     * @param productID productId
     * @return product
     */


    /**
     * Method tim kim product dua tren category name
     *
     * @param category_name
     * @return list product
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/category/{category_name}")
    public ResponseEntity<ObjectResponse> getProductByCategory(@PathVariable("category_name") String category_name) {
        try {
            List<Product> products = productService.getProductsByCategory(category_name);
            if (products == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Danh sách sản phẩm không tồn tại", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Lấy danh sách sản phẩm thành công", products));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Lấy danh sách sản phẩm thành công", null));
        }
    }

    /**
     * Method update product
     *
     * @param id productId
     * @return message success or fail
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{product_id}")
    public ResponseEntity<ObjectResponse> updateProduct(@PathVariable("product_id") String id, @RequestBody Product product) {
        try {
            Product existingProduct = productService.getProductById(id);
            if (existingProduct == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Sản phẩm không tồn tại", null));
            }
            if (product.getProductName() != null) {
                existingProduct.setProductName(product.getProductName());
            }
            if (product.getBathStone() != null) {
                existingProduct.setBathStone(product.getBathStone());
            }
            if (product.getBrand() != null) {
                existingProduct.setBrand(product.getBrand());
            }
            if (product.getGoldType() != null) {
                existingProduct.setGoldType(product.getGoldType());
            }
            if (product.getGoldWeight() > 0.0) {
                existingProduct.setGoldWeight(product.getGoldWeight());
            }
            if (product.getShapeDiamond() != null) {
                existingProduct.setShapeDiamond(product.getShapeDiamond());
            }
            if (product.getDimensionsDiamond() > 0.0) {
                existingProduct.setDimensionsDiamond(product.getDimensionsDiamond());
            }
            if (product.getMessage() != null) {
                existingProduct.setMessage(product.getMessage());
            }
            if (product.getOldGold() != null) {
                existingProduct.setOldGold(product.getOldGold());
            }
            if (product.getProductType() != null) {
                existingProduct.setProductType(product.getProductType());
            }
            if (product.getQuantity() > 0) {
                existingProduct.setQuantity(product.getQuantity());
            }
            if (product.getQuantityStonesOfDiamond() > 0) {
                existingProduct.setQuantityStonesOfDiamond(product.getQuantityStonesOfDiamond());
            }
            if (product.getOriginalPrice() > 0 && product.getWagePrice() > 0 && product.getRatio() > 0) {
                existingProduct.setOriginalPrice(product.getOriginalPrice());
                existingProduct.setWagePrice(product.getWagePrice());
                existingProduct.setRatio(product.getRatio());
                existingProduct.setTotalPrice((product.getWagePrice() + product.getOriginalPrice()) * (1 + product.getRatio()));
            } else if (product.getOriginalPrice() > 0 && product.getWagePrice() > 0 && product.getRatio() <= 0) {
                existingProduct.setOriginalPrice(product.getOriginalPrice());
                existingProduct.setWagePrice(product.getWagePrice());
                existingProduct.setTotalPrice((product.getOriginalPrice() + product.getWagePrice()) * (1 + existingProduct.getRatio()));
            } else if (product.getOriginalPrice() > 0 && product.getWagePrice() <= 0 && product.getRatio() <= 0) {
                existingProduct.setOriginalPrice(product.getOriginalPrice());
                existingProduct.setTotalPrice((product.getOriginalPrice() + existingProduct.getWagePrice()) * (1 + existingProduct.getRatio()));
            } else if (product.getOriginalPrice() > 0 && product.getWagePrice() <= 0 && product.getRatio() > 0) {
                existingProduct.setOriginalPrice(product.getOriginalPrice());
                existingProduct.setRatio(product.getRatio());
                existingProduct.setTotalPrice((product.getOriginalPrice() + existingProduct.getWagePrice()) * (1 + product.getRatio()));
            } else if (product.getOriginalPrice() <= 0 && product.getWagePrice() > 0 && product.getRatio() > 0) {
                existingProduct.setWagePrice(product.getWagePrice());
                existingProduct.setRatio(product.getRatio());
                existingProduct.setTotalPrice((existingProduct.getOriginalPrice() + product.getWagePrice()) * (1 + product.getRatio()));
            } else if (product.getOriginalPrice() <= 0 && product.getWagePrice() <= 0 && product.getRatio() > 0) {
                existingProduct.setRatio(product.getRatio());
                existingProduct.setTotalPrice((existingProduct.getOriginalPrice() + existingProduct.getWagePrice()) * (1 + product.getRatio()));
            } else if (product.getOriginalPrice() <= 0 && product.getWagePrice() > 0 && product.getRatio() <= 0) {
                existingProduct.setWagePrice(product.getWagePrice());
                existingProduct.setTotalPrice((existingProduct.getOriginalPrice() + product.getWagePrice()) * (1 + existingProduct.getRatio()));
            }
            if (product.getSizes() != null) {
                updateProductSizes(existingProduct, product.getSizes());
            }
            if (product.getProductImages() != null) {
                updateProductThumbnails(existingProduct, product.getProductImages());
            }
            productService.updateProduct(id, existingProduct);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Chỉnh sửa thành công", existingProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Chỉnh sửa thất bại", null));
        }
    }

    /**
     * Method xoa product bang cach thay doi trang thai
     * true => false
     *
     * @return message success or fail
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{product_id}")
    public ResponseEntity<ObjectResponse> deleteProductStatus(@PathVariable("product_id") String productID) {
        try {
            Product product = productService.getProductById(productID);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Sản phẩm không tồn tại", null));
            } else {
                productService.deleteProductStatus(product.getProductID());
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Xóa Thành Công với ID " + productID, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Xoá sản phẩm thất bại", null));
        }
    }

    /**
     * Method xoa product bang cach thay doi trang thai
     * true => false
     * thuc hien tren nhiu doi tuong
     *
     * @return message success or fail
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete_products")
    public ResponseEntity<ObjectResponse> deleteProducts(@RequestBody List<String> prodcutIds) {
        try {
            if (prodcutIds == null || prodcutIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Vui lòng chọn sản phẩm muốn xóa", null));
            }
            for (int i = 0; i < prodcutIds.size(); i++) {
                String productID = prodcutIds.get(i);
                Product product = productService.getProductById(productID);
                if (product == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Sản phẩm không tồn tại", null));
                } else {
                    product.setStatus(false);
                }
            }
            productService.deleteProducts(prodcutIds);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Xóa Thành Công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Xóa Thất bại", null));
        }
    }

    /**
     * method update size cuar product
     *
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
            sizeService.deleteSize(sizeToRemove.getSizeID());
        }
    }

    /**
     * method update image cuar product
     *
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
            thumnailService.deleteThumnail(thumnailToRemove.getImageId());
        }
    }

}
