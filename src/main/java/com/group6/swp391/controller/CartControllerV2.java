package com.group6.swp391.controller;

import com.group6.swp391.model.Cart;
import com.group6.swp391.service.CartServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/swp391/api/carts")
@CrossOrigin(origins = "*")
public class CartControllerV2 {
    @Autowired
    CartServiceImp cartServiceImp;

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

}
