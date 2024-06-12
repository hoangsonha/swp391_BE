package com.group6.swp391.service;

import com.group6.swp391.model.Cart;
import com.group6.swp391.model.CartItem;
import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.ProductCustomize;
import com.group6.swp391.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CartServiceImp implements CartService {
    @Autowired CartRepository cartRepository;

    @Autowired ProductCustomizeServiceImp productCustomizeServiceImp;

    @Autowired UserServiceImp userServiceImp;

    @Autowired DiamondServiceImp diamondServiceImp;

    @Override
    public void addCart(int userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId);
        if(cart == null) {
            cart = new Cart();
            cart.setUser(userServiceImp.getUserByID(userId));
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setQuantity(1);

        if(productId.startsWith("P")||productId.startsWith("p")) {
            ProductCustomize productCustomize = productCustomizeServiceImp.getProductCustomizeById(productId);
            cartItem.setProductCustomize(productCustomize);
            cartItem.setTotalPrice(productCustomize.getTotalPrice()*cartItem.getQuantity());
        } else {
            Diamond diamond = diamondServiceImp.getDiamondByDiamondID(productId);
            cartItem.setDiamond(diamond);
            cartItem.setTotalPrice(diamond.getTotalPrice()*cartItem.getQuantity());
        }
        cart.addItem(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public Cart getCart(int userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }
}
