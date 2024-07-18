package com.group6.swp391.response;

import com.group6.swp391.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private int cartId;
    private UserRespone user;
    private int quantity;
    private Set<CartItem> items;


}
