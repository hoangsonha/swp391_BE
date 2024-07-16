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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/swp391/api/carts")
@CrossOrigin(origins = "*")
public class CartController {
    @Autowired CartServiceImp cartServiceImp;
    @Autowired CartItemRepository CartItemRepository;
    @Autowired UserService userService;

    /**
     * Method add to cart
     * method này dành cho add kim cương
     * @param args Nhan vao userId and diamondId
     * @return success or field
     */
    @PreAuthorize("hasRole('USER')")
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

    /**
     * Method tìm kiem cart dua tren user
     * @param args userId
     * @return List cart_item in cart
     */
    @PreAuthorize("hasRole('USER')")
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

    /**
     * Lay ra tat car cart trong data
     * ko can thiet, chua dc su dung
     * @param args
     * @return List cart
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all_carts")
    public ResponseEntity<List<Cart>> getAll() {
        List<Cart>carts = cartServiceImp.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    /**
     * Method xoa bat cu mot cart_item nao
     * @param args cart_item_id
     * @return message success or fail
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("delete/{cart_item_id}")
    public ResponseEntity<String> removeCart(@PathVariable("cart_item_id") int itemId) {
        try {
            cartServiceImp.removeCart(itemId);
            return ResponseEntity.ok().body("Item removed from cart successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Method dem so luong cart_item cos trong mot cart doi voi mot user
     * @param args userId
     * @return number integer
     */
    @PreAuthorize("hasRole('USER')")
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
