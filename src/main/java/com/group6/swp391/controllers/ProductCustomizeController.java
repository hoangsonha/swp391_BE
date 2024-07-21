package com.group6.swp391.controllers;

import com.group6.swp391.pojos.Diamond;
import com.group6.swp391.pojos.Product;
import com.group6.swp391.pojos.ProductCustomize;
import com.group6.swp391.repositories.DiamondRepository;
import com.group6.swp391.requests.CustomizeRequest;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swp391/api/productcustomes")
@CrossOrigin(origins = "*")
public class ProductCustomizeController {

    @Autowired ProductCustomizeServiceImp productCustomizeServiceImp;
    @Autowired ProductServiceImp productServiceImp;
    @Autowired DiamondServiceImp diamondServiceImp;
    @Autowired DiamondRepository diamondRepository;
    @Autowired CategoryServiceImp categoryServiceImp;
    @Autowired CartServiceImp cartServiceImp;

    /**
     * method tao customize
     * nhan ve doi tuong custmoize va tien hang add cart
     * @return message success or fail
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create_customizeProduct/{userId}")
    public ResponseEntity<ObjectResponse> createProductCustome(@PathVariable("userId") int userId,
                                                               @RequestBody @Valid CustomizeRequest customizeRequest) {
        try {
            if(customizeRequest == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Sản phẩm chỉnh  chọn không được trống", null));
            }
            Product product = productServiceImp.getProductById(customizeRequest.getProductId());
            if(product == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Sản phẩm không tồn tại", null));
            }
            Diamond diamond = diamondServiceImp.getDiamondByDiamondID(customizeRequest.getDiamondId());
            if(diamond == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Kim Cương không tồn tại", null));
            }
            ProductCustomize cus = new ProductCustomize();
            cus.setProdcutCustomId("P" + product.getProductID() + "-" + diamond.getDiamondID());
            cus.setProduct(product);
            cus.setDiamond(diamond);
            cus.setSize(customizeRequest.getSize());
            cus.setTotalPrice(product.getTotalPrice() + diamond.getTotalPrice());
            productCustomizeServiceImp.createProductCustomize(cus);
            cartServiceImp.addCart(userId, cus.getProdcutCustomId());
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Tạo thành công", cus));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    /**
     * method tim kim mot customize
     * @param id productcustomizeId
     * @return productcustomize
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/productcustomeize_id/{id}")
    public ResponseEntity<ObjectResponse> getProductCustomById(@PathVariable("id") String id) {
        try {
            ProductCustomize exsitProductCustomize = productCustomizeServiceImp.getProductCustomizeById(id);
            if(exsitProductCustomize == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Sản phẩm tùy chỉnh không tồn tại", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Sản phẩm tùy chỉnh", exsitProductCustomize));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    /**
     * method delete customize
     * @return message success or fail
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{productcustomize_id}")
    public ResponseEntity<ObjectResponse> deleteProductCustomById(@PathVariable("productcustomize_id") String id) {
        try {
            ProductCustomize productCustomize = productCustomizeServiceImp.getProductCustomizeById(id);
            if(productCustomize == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Sản phẩm tùy chỉnh không tồn tại", null));
            } else {
                productCustomizeServiceImp.deleteProductCustomize(id);
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Xóa Thành Công", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    /**
     * method update customize
     * @param id productcustomizeId
     * @return message success or fail
     */
//    @PreAuthorize("hasRole('USER')")
//    @PutMapping("update/{productcustomize_id}")
//    public ResponseEntity<?> updateProductCustome(@RequestBody ProductCustomize productCustomize,
//                                                  @PathVariable("productcustomize_id") String id) {
//        try {
//            ProductCustomize existingProductCustomize = productCustomizeServiceImp.getProductCustomizeById(id);
//            if(productCustomize == null) {
//                return ResponseEntity.badRequest().body("Product custom not found");
//            }
//            existingProductCustomize.setProdcutCustomId(productCustomize.getProdcutCustomId());
//            existingProductCustomize.setProduct(productCustomize.getProduct());
//            existingProductCustomize.setDiamond(productCustomize.getDiamond());
//            existingProductCustomize.setSize(productCustomize.getSize());
//            existingProductCustomize.setTotalPrice(productCustomize.getTotalPrice());
//            productCustomizeServiceImp.updateProductCustomize(existingProductCustomize.getProdcutCustomId(), existingProductCustomize);
//            return ResponseEntity.ok().body("Product custom updated");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    /**
     * method get all customize
     * @return list customize
     */
//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/all_productcustomize")
//    public ResponseEntity<List<ProductCustomize>> getAllProductCustomize() {
//        try {
//            List<ProductCustomize> productCustomizeList = productCustomizeServiceImp.getAllProductCustomize();
//            if(productCustomizeList == null) {
//                return ResponseEntity.badRequest().body(null);
//            } else {
//                return ResponseEntity.ok().body(productCustomizeList);
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
}
