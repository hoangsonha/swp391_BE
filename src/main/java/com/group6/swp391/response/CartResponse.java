package com.group6.swp391.response;

import com.group6.swp391.model.CartItem;
import com.group6.swp391.model.Diamond;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private int cartId;
    private int userId;
    private int quantity;
    private List<CartItem> cartItems;
}
