package com.group6.swp391.service;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.Points;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.OrderRepository;
import com.group6.swp391.repository.PointsRepository;
import com.group6.swp391.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${devide.point}") private int devidePoint;

    @Override
    public Points createPoints(int userId, int orderId) {
        try {
            User userExisting = userRepository.getUserByUserID(userId);
            Order orderExisting = orderRepository.getOrderByOrderID(orderId);
            if(userExisting == null && orderExisting == null){
                throw new RuntimeException("User Or Order not null");
            }
            Points points = pointsRepository.findByOrderId(orderId);
            if(points != null){
                points.setPoint(orderExisting.getPrice()/devidePoint);
            } else {
                points = new Points();
                points.setPoint(orderExisting.getPrice()/devidePoint);
                points.setUser(userExisting);
                points.setOrder(orderExisting);
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
        Points points = pointsRepository.findByOrderId(order.getOrderID());
        if(points == null){
            points = new Points();
            points.setUser(user);
            points.setOrder(order);
            points.setUsedPoints(userPoint);
        }
        points.setUsedPoints(userPoint);
        return pointsRepository.save(points);
    }

    @Override
    public Points getPointByUser(int userId) {
        return pointsRepository.findByUser(userId);
    }

}
