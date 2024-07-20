package com.group6.swp391.controller;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Product;
import com.group6.swp391.model.ProductCustomize;
import com.group6.swp391.repository.DiamondRepository;
import com.group6.swp391.request.CustomizeRequest;
import com.group6.swp391.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> createProductCustome(@PathVariable("userId") int userId,
                                                  @RequestBody @Valid CustomizeRequest customizeRequest) {
        try {
            if(customizeRequest == null) {
                return ResponseEntity.badRequest().body("Custome Request can't be null");
            }
            Product product = productServiceImp.getProductById(customizeRequest.getProductId());
            if(product == null) {
                return ResponseEntity.badRequest().body("Product not found");
            }
            Diamond diamond = diamondServiceImp.getDiamondByDiamondID(customizeRequest.getDiamondId());
            if(diamond == null) {
                return ResponseEntity.badRequest().body("Diamond not found");
            }
            ProductCustomize cus = new ProductCustomize();
            cus.setProdcutCustomId("P" + product.getProductID() + "-" + diamond.getDiamondID());
            cus.setProduct(product);
            cus.setDiamond(diamond);
            cus.setSize(customizeRequest.getSize());
            cus.setTotalPrice(product.getTotalPrice() + diamond.getTotalPrice());
            productCustomizeServiceImp.createProductCustomize(cus);
            cartServiceImp.addCart(userId, cus.getProdcutCustomId());
            return ResponseEntity.ok().body("Custome created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * method tim kim mot customize
     * @param id productcustomizeId
     * @return productcustomize
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/productcustomeize_id/{id}")
    public ResponseEntity<ProductCustomize> getProductCustomById(@PathVariable("id") String id) {
        try {
            ProductCustomize exsitProductCustomize = productCustomizeServiceImp.getProductCustomizeById(id);
            if(exsitProductCustomize == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(exsitProductCustomize);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * method delete customize
     * @return message success or fail
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{productcustomize_id}")
    public ResponseEntity<String> deleteProductCustomById(@PathVariable("productcustomize_id") String id) {
        try {
            ProductCustomize productCustomize = productCustomizeServiceImp.getProductCustomizeById(id);
            if(productCustomize == null) {
                return ResponseEntity.badRequest().body("Product custome not found");
            } else {
                productCustomizeServiceImp.deleteProductCustomize(id);
                return ResponseEntity.ok().body("Product custom deleted");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * method update customize
     * @param id productcustomizeId
     * @return message success or fail
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("update/{productcustomize_id}")
    public ResponseEntity<?> updateProductCustome(@RequestBody ProductCustomize productCustomize,
                                                  @PathVariable("productcustomize_id") String id) {
        try {
            ProductCustomize existingProductCustomize = productCustomizeServiceImp.getProductCustomizeById(id);
            if(productCustomize == null) {
                return ResponseEntity.badRequest().body("Product custom not found");
            }
            existingProductCustomize.setProdcutCustomId(productCustomize.getProdcutCustomId());
            existingProductCustomize.setProduct(productCustomize.getProduct());
            existingProductCustomize.setDiamond(productCustomize.getDiamond());
            existingProductCustomize.setSize(productCustomize.getSize());
            existingProductCustomize.setTotalPrice(productCustomize.getTotalPrice());
            productCustomizeServiceImp.updateProductCustomize(existingProductCustomize.getProdcutCustomId(), existingProductCustomize);
            return ResponseEntity.ok().body("Product custom updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * method get all customize
     * @return list customize
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all_productcustomize")
    public ResponseEntity<List<ProductCustomize>> getAllProductCustomize() {
        try {
            List<ProductCustomize> productCustomizeList = productCustomizeServiceImp.getAllProductCustomize();
            if(productCustomizeList == null) {
                return ResponseEntity.badRequest().body(null);
            } else {
                return ResponseEntity.ok().body(productCustomizeList);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
