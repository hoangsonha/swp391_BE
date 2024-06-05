package com.group6.swp391.service;

import com.group6.swp391.model.Order;
import com.group6.swp391.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired private OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderByOrderID(int orderID) {
        return orderRepository.getOrderByOrderID(orderID);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

}
