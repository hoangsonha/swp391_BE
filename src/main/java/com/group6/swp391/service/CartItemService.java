package com.group6.swp391.service;

import com.group6.swp391.model.Cart;
import com.group6.swp391.model.CartItem;

import java.util.List;

public interface CartItemService {

    List<CartItem> getCartItemsByCartID(int cartID);

    void deleteByCart(int cart);

}
