package com.group6.swp391.services;

import com.group6.swp391.pojos.Order;
import com.group6.swp391.pojos.OrderDetail;

import java.util.List;

public interface OrderService {

    Order getOrderByOrderID(int orderID);

    Order save(Order order);

    List<Order> getAllOrder();

    void markOrderAsDeleted(int orderID,String status, String reason);

    List<Order> getOrderByDiamondID(String diamondID);

    List<Order> getOrderByUserID(int userID);

    Order saveOrder(Order order, List<OrderDetail> orderDetails);

    List<Order> getNewestOrderStaff(int staffId, String status);

    List<Order> getNewestOrderAdmin(String status);
    List<Order> getNewestOrderDelivery(int deliveryId, String status);

    List<Order> getOrdersByStatus(String status);

    List<Order> getStatusByUser(int userId, String status);

    Object updateStatus(int orderID, String status, String reason);

    Order updateOrderStatus(int orderID, String status, String reason);

    Order updateOrderStatus(int orderID, String status);

    long getPendingDeliveryOrderCount();

    List<Order> getAllDelivery(int deliveryId);

    List<Order> getAllWithStaff(int staffId);

    // OrderDetailService
    List<OrderDetail> getOrderDetailsByOrderID(int orderID);
}
