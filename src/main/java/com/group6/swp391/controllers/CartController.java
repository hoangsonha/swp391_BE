package com.group6.swp391.controllers;

import com.group6.swp391.pojos.Cart;
import com.group6.swp391.pojos.User;
import com.group6.swp391.repositories.CartItemRepository;
import com.group6.swp391.requests.CartRequest;
import com.group6.swp391.responses.CartResponse;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.responses.UserRespone;
import com.group6.swp391.services.CartServiceImp;
import com.group6.swp391.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
     * @param cartRequest
     * @return success or field
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add_cart")
    public ResponseEntity<ObjectResponse> addCart(@RequestBody @Valid CartRequest cartRequest) {
        try {
            if(cartRequest.getProductId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Kim Cương Không Tồn Tại", null));
            }

            cartServiceImp.addCart(cartRequest.getUserId(), cartRequest.getProductId());
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Thêm Giỏ Hàng Thành Công",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed","Message: " +  e.getMessage(), null));
        }
    }


    /**
     * Method tìm kiem cart dua tren user
     * @return List cart_item in cart
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cartUser/{user_id}")
    public ResponseEntity<ObjectResponse> getCartByUserId(@PathVariable("user_id") int userId) {
        try {
            User userExisting = userService.getUserByID(userId);
            if(userExisting == null) {
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed","Người Dùng Không Tồn Tại", null));
            }
            Cart exsitingCart = cartServiceImp.getCart(userId);
            if (exsitingCart == null) {
                exsitingCart = new Cart();
                exsitingCart.setUser(userExisting);
                cartServiceImp.create(exsitingCart);
            }
            CartResponse cartResponse = new CartResponse();
            cartResponse.setCartId(exsitingCart.getCartId());
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
            cartResponse.setQuantity(exsitingCart.getItems().size());
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Giỏ Hàng Với Người Dùng" + userExisting.getFirstName() + userExisting.getLastName(), cartResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed","Message: " + e.getMessage(), null));
        }
    }


    /**
     * Lay ra tat car cart trong data
     * ko can thiet, chua dc su dung
     * @return List cart
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all_carts")
    public ResponseEntity<ObjectResponse> getAll() {
        try {
            List<Cart> carts = cartServiceImp.getAllCarts();
            if(carts == null || carts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Giỏ Hàng Trống","Giỏ Hàng Trống"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Giỏ Hàng ",carts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed","Message: " +  e.getMessage(), null));
        }
    }


    /**
     * Method xoa bat cu mot cart_item nao
     * @param itemId cart_item_id
     * @return message success or fail
     */

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("delete/{cart_item_id}")
    public ResponseEntity<ObjectResponse> removeCart(@PathVariable("cart_item_id") int itemId) {
        try {
            cartServiceImp.removeCart(itemId);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Xóa Thành Công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed","Message: " + e.getMessage(), null));
        }
    }

    /**
     * Method dem so luong cart_item cos trong mot cart doi voi mot user
     * @param id userId
     * @return number integer
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{user_id}")
    public ResponseEntity<ObjectResponse> countQuantityIncart(@PathVariable("user_id") int id) {
        try {
            Cart exsitingCart = cartServiceImp.getCart(id);
            if (exsitingCart == null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Failed", "Giỏ hàng không tồn tại", null));
            }
            int count = exsitingCart.getItems().size();
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Số lượng sản phẩm có trong giỏ hàng", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Success", "Message: " + e.getMessage(), null));
        }
    }
}
