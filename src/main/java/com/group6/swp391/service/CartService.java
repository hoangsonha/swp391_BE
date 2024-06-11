package com.group6.swp391.service;

import com.group6.swp391.model.Cart;

public interface CartService {
    void addCart(int userId, String productId);
    Cart getCart(int userId);

}
