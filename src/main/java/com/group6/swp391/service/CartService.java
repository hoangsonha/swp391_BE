package com.group6.swp391.service;

import com.group6.swp391.model.Cart;

import java.util.List;

public interface CartService {
    void addCart(int userId, String productId);
    Cart getCart(int userId);
    List<Cart> getAllCarts();
    void removeCart(int cartItemId);

}
