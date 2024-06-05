package com.group6.swp391.service;

import com.group6.swp391.model.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    Order getOrderByOrderID(int orderID);

    void save(Order order);

    List<Order> getAllOrder();

}
