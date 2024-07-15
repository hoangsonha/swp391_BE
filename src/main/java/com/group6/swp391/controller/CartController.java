package com.group6.swp391.controller;

import com.group6.swp391.model.Cart;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.CartItemRepository;
import com.group6.swp391.request.CartRequest;
import com.group6.swp391.response.CartResponse;
import com.group6.swp391.response.UserRespone;
import com.group6.swp391.service.CartServiceImp;
import com.group6.swp391.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swp391/api/carts")
@CrossOrigin(origins = "*")
public class CartController {
    @Autowired CartServiceImp cartServiceImp;
    @Autowired CartItemRepository CartItemRepository;
    @Autowired UserService userService;


    @PostMapping("/add_cart")
    public ResponseEntity<?> addCart(@RequestBody @Valid CartRequest cartRequest) {
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
            CartResponse cartResponse = new CartResponse();
            cartResponse.setCartId(exsitingCart.getCartId());
            User userExisting = userService.getUserByID(userId);
            UserRespone userRespone = new UserRespone();
            userRespone.setUserID(userExisting.getUserID());
            userRespone.setFirstName(userExisting.getFirstName());
            userRespone.setLastName(userExisting.getLastName());
            userRespone.setEmail(userExisting.getEmail());
            userRespone.setGender(userExisting.getGender());
            userRespone.setAddress(userExisting.getAddress());
            userRespone.setPhone(userExisting.getPhone());
            userRespone.setYearOfBirth(userExisting.getYearOfBirth());
            userRespone.setPoints(userExisting.getPoints());
            cartResponse.setUser(userRespone);
            cartResponse.setItems(exsitingCart.getItems());
            return ResponseEntity.ok(cartResponse);
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

    @GetMapping("/{user_id}")
    public ResponseEntity<Integer> getCart(@PathVariable("user_id") int id) {
        try {
            Cart exsitingCart = cartServiceImp.getCart(id);
            if (exsitingCart == null) {
                return ResponseEntity.badRequest().body(0);
            }
            int count = exsitingCart.getItems().size();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0);
        }
    }
}
