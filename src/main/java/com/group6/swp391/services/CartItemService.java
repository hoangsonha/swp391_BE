package com.group6.swp391.services;

import com.group6.swp391.pojos.CartItem;

import java.util.List;

public interface CartItemService {

    List<CartItem> getCartItemsByCartID(int cartID);

    void deleteByCart(int cart);

}
