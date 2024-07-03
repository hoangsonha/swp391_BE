package com.group6.swp391.service;

import com.group6.swp391.model.*;
import com.group6.swp391.repository.CartItemRepository;
import com.group6.swp391.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class  CartServiceImp implements CartService {
    @Autowired CartRepository cartRepository;

    @Autowired ProductCustomizeServiceImp productCustomizeServiceImp;

    @Autowired UserServiceImp userServiceImp;

    @Autowired DiamondServiceImp diamondServiceImp;

    @Autowired CartItemRepository CartItemRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired CollectionService collectionService;

    @Override
    public void addCart(int userId, String productId) {
        if(isDiamondinCart(userId, productId)) {
            throw new RuntimeException("diamond already existing in your cart");
        }
        Cart cart = cartRepository.findByUserId(userId);
        if(cart == null) {
            cart = new Cart();
            cart.setUser(userServiceImp.getUserByID(userId));
        }

        boolean cartItemExist = CartItemRepository.existsByUserIdAndProductCustomizeIdOrDiamondId(userId, productId);
        if(cartItemExist) {
            throw new RuntimeException("Product exist");
        }
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setQuantity(1);

        if(productId.startsWith("P")||productId.startsWith("p")) {
            ProductCustomize productCustomize = productCustomizeServiceImp.getProductCustomizeById(productId);
            cartItem.setProductCustomize(productCustomize);
            cartItem.setTotalPrice(productCustomize.getTotalPrice()*cartItem.getQuantity());
        } else if(productId.startsWith("S")||productId.startsWith("s")) {
            Collection collectionExisting = collectionService.getCollection(productId);
            cartItem.setCollection(collectionExisting);
            cartItem.setTotalPrice(collectionExisting.getPrice()*cartItem.getQuantity());
        } else {
            Diamond diamond = diamondServiceImp.getDiamondByDiamondID(productId);
            cartItem.setDiamondAdd(diamond);
            cartItem.setTotalPrice(diamond.getTotalPrice()*cartItem.getQuantity());
        }
        cart.addItem(cartItem);
        cartRepository.save(cart);
    }

    public boolean isDiamondinCart(int userId, String productCustomizeId) {
        String diamondId = extractDiamondId(productCustomizeId);
        List<CartItem> carts = cartItemRepository.findCartByDupliCateDiamond(userId, diamondId);
        return !carts.isEmpty();
    }

    private String extractDiamondId(String productCustomizeId) {
        String[] parts = productCustomizeId.split("-");
        return parts.length > 1 ? parts[1] : "";
    }

    @Override
    public Cart getCart(int userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public void removeCart(int cartItemId) {
        CartItem cartItemToRemove = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItemToRemove != null) {
            Cart cart = cartItemToRemove.getCart();
            if (cart != null) {
                cart.removeItem(cartItemToRemove);
                cartRepository.save(cart);
            }
        }
    }

    @Override
    public void clearCart(int userID) {
        Cart cart = cartRepository.findByUserId(userID);
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }
}
