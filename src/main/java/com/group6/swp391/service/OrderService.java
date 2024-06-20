package com.group6.swp391.service;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.OrderDetail;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    Order getOrderByOrderID(int orderID);

    Order save(Order order);

    List<Order> getAllOrder();

    void markOrderAsDeleted(int orderID);

    List<Order> getOrderByDiamondID(String diamondID);

    List<Order> getOrderByUserID(int userID);

    Order saveOrder(Order order, List<OrderDetail> orderDetails);

    List<Order> getNewestOrder(String status);

    List<Order> getOrdersByStatus(String status);

    Object updateStatus(int orderID, String status);
}
