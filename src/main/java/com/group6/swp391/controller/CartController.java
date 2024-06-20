package com.group6.swp391.controller;

import com.group6.swp391.model.Cart;
import com.group6.swp391.model.CartItem;
import com.group6.swp391.repository.CartItemRepository;
import com.group6.swp391.request.CartRequest;
import com.group6.swp391.service.CartServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swp391/api/carts")
@CrossOrigin(origins = "*")
public class CartController {
    @Autowired
    CartServiceImp cartServiceImp;
    @Autowired
    CartItemRepository CartItemRepository;

    @PostMapping("/add_cart")
    public ResponseEntity<?> addCart(@RequestBody CartRequest cartRequest) {
        try {
            if(cartRequest.getProductId() == null) {
                return ResponseEntity.badRequest().body("Product is required");
            }

            cartServiceImp.addCart(cartRequest.getUserId(), cartRequest.getProductId());
            return ResponseEntity.ok().body("added cart successfully");
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

    @DeleteMapping("delete/{cart_item_id}")
    public ResponseEntity<String> removeCart(@PathVariable("cart_item_id") int itemId) {
        try {
            cartServiceImp.removeCart(itemId);
            return ResponseEntity.ok().body("Item removed from cart successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
