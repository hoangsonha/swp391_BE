package com.group6.swp391.service;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.OrderDetail;

import java.util.List;

public interface OrderService {

    Order getOrderByOrderID(int orderID);

    Order save(Order order);

    List<Order> getAllOrder();

    void markOrderAsDeleted(int orderID,String status, String reason);

    List<Order> getOrderByDiamondID(String diamondID);

    List<Order> getOrderByUserID(int userID);

    Order saveOrder(Order order, List<OrderDetail> orderDetails);

    List<Order> getNewestOrder(String status);

    List<Order> getOrdersByStatus(String status);

    List<Order> getStatusByUser(int userId, String status);

    Object updateStatus(int orderID, String status, String reason);

    Order updateStatus(int orderID, String status);

    double getByMonth(int month, int year);
}
