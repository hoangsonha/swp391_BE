package com.group6.swp391.service;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.Points;
import com.group6.swp391.model.User;
import com.group6.swp391.repository.OrderRepository;
import com.group6.swp391.repository.PointsRepository;
import com.group6.swp391.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Points createPoints(int userId, int orderId, double usedPoint) {
        try {
            User userExisting = userRepository.getUserByUserID(userId);
            Order orderExisting = orderRepository.getOrderByOrderID(orderId);
            if(userExisting == null && orderExisting == null){
                throw new RuntimeException("User Or Order not null");
            }
            Points points = new Points();
            points.setUser(userExisting);
            points.setOrder(orderExisting);
            points.setUsedPoints(usedPoint);
            points.setPoint(orderExisting.getPrice()/100);
            pointsRepository.save(points);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Points> getAll() {
        return pointsRepository.findAll();
    }

    @Override
    public List<Points> getByUser(int userId) {
        return pointsRepository.findAll();
    }

    @Override
    public Points getById(int id) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void updatePoints(int userId, double point) {
        User user = userRepository.getUserByUserID(userId);
        if(user == null){
            throw new RuntimeException("User Not Null");
        }
        Points pointsExist = pointsRepository.findByUser(userId);

    }
}
