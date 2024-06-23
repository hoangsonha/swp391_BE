package com.group6.swp391.controller;

import com.group6.swp391.model.Order;
import com.group6.swp391.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/swp391/api/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {

    @Autowired
    OrderService orderService;

    @GetMapping("/pending-delivery")
    public ResponseEntity<List<Order>> getPendingDeliveryOrders() {
        List<Order> orders = orderService.getOrdersByStatus("Chờ giao hàng");
        return ResponseEntity.ok(orders);
    }



}
