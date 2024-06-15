package com.group6.swp391.service;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.OrderDetail;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    Order getOrderByOrderID(int orderID);

    Order save(Order order);

    List<Order> getAllOrder();

    void markOrderAsDeleted(int orderID);

    void createOrderDetails(List<OrderDetail> orderDetails);

    List<Order> getOrderByDiamondID(String diamondID);
}
