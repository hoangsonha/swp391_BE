package com.group6.swp391.controller;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.Points;
import com.group6.swp391.model.User;
import com.group6.swp391.request.PointRequest;
import com.group6.swp391.service.OrderServiceImp;
import com.group6.swp391.service.PointsServiceImp;
import com.group6.swp391.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/swp391/api/points")
@CrossOrigin(origins = "*")
public class PointsController {
    @Autowired
    PointsServiceImp pointsServiceImp;
    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    OrderServiceImp orderServiceImp;

    @PostMapping("/create_point")
    public ResponseEntity<Points> creatPoints(@RequestBody PointRequest pointRequest) {
        try {
            if(pointRequest == null) {
                return ResponseEntity.status(400).body(null);
            }
            User userExisting = userServiceImp.getUserByID(pointRequest.getUserId());
            Order orderExisting = orderServiceImp.getOrderByOrderID(pointRequest.getOrderId());
            if(userExisting == null && orderExisting == null) {
                return ResponseEntity.status(400).body(null);
            }
            pointsServiceImp.createPoints(userExisting.getUserID(), orderExisting.getOrderID());
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
