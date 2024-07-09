package com.group6.swp391.service;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.Points;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.OrderRepository;
import com.group6.swp391.repository.PointsRepository;
import com.group6.swp391.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class PointsServiceImp implements PointsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    PointsRepository pointsRepository;

    @Override
    public Points createPoints(int userId, int orderId) {
        try {
            User userExisting = userRepository.getUserByUserID(userId);
            Order orderExisting = orderRepository.getOrderByOrderID(orderId);
            if(userExisting == null && orderExisting == null){
                throw new RuntimeException("User Or Order not null");
            }
            Points points = pointsRepository.findByOrderId(orderId);
            if(userExisting != null){
                points.setPoint(orderExisting.getPrice()/100000000);
            } else {
                Points newPoints = new Points();
                newPoints.setPoint(orderExisting.getPrice()/100000000);
                newPoints.setUser(userExisting);
                newPoints.setOrder(orderExisting);
            }
            pointsRepository.save(points);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Points getUserPoints(int userId, int orderId, double userPoint) {
        User user = userRepository.getUserByUserID(userId);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        Order order = orderRepository.getOrderByOrderID(orderId);
        if (order == null) {
            throw new RuntimeException("Order Not Found");
        }
        Points points = pointsRepository.findByUser(userId);
        if(points == null){
            throw new RuntimeException("Points Not Found");
        }
        Points usedPoints = new Points();
        usedPoints.setUser(user);
        usedPoints.setOrder(order);
        usedPoints.setUsedPoints(userPoint);
        return pointsRepository.save(usedPoints);
    }

    @Override
    public Points getPointByUser(int userId) {
        return pointsRepository.findByUser(userId);
    }

}
