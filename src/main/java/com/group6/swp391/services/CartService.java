package com.group6.swp391.services;

import com.group6.swp391.pojos.Cart;

import java.util.List;

public interface CartService {
    Cart create(Cart cart);
    void addCart(int userId, String productId);

    Cart getCart(int userId);

    List<Cart> getAllCarts();

    void removeCart(int cartItemId);

    void clearCart(int userID);

}
