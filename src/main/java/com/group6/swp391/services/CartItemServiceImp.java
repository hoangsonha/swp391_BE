package com.group6.swp391.services;

import com.group6.swp391.pojos.CartItem;
import com.group6.swp391.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImp implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public List<CartItem> getCartItemsByCartID(int cartID) {
        return cartItemRepository.findByCartCartId(cartID);
    }

    @Override
    public void deleteByCart(int cart) {
        List<CartItem> cartItem = cartItemRepository.findByCart(cart);
        if(cartItem == null) {
            throw new RuntimeException("CartItem not Found");
        }
        cartItemRepository.deleteAll(cartItem);
    }
}
