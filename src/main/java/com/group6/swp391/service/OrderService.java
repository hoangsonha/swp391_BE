package com.group6.swp391.service;

import com.group6.swp391.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createOrder(Order order);

    Order getOrderByOrderID(int orderID);

    Order save(Order order);

    List<Order> getAllOrder();

}
