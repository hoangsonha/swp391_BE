package com.group6.swp391.controller;

import com.group6.swp391.model.Cart;
import com.group6.swp391.service.CartServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swp391/api/carts")
@CrossOrigin(origins = "*")
public class CartControllerV2 {
    @Autowired
    CartServiceImp cartServiceImp;

    @GetMapping("/add_cart/{user_id}")
    public ResponseEntity<?> addCart(@PathVariable("user_id") int userId,@RequestParam String productId) {
        try {
            // Validate productId is not null or empty
            if (productId == null || productId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Product ID cannot be null or empty");
            }
            cartServiceImp.addCart(userId, productId);
            return ResponseEntity.ok().body("Added to cart successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/cartUser/{user_id}")
    public ResponseEntity<?> getCartByUserId(@PathVariable("user_id") int userId) {
        try {
            Cart exsitingCart = cartServiceImp.getCart(userId);
            if (exsitingCart == null) {
                return ResponseEntity.badRequest().body("user not found");
            }
            return ResponseEntity.ok(exsitingCart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all_carts")
    public ResponseEntity<List<Cart>> getAll() {
        List<Cart>carts = cartServiceImp.getAllCarts();
        return ResponseEntity.ok(carts);
    }


}
