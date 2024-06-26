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

    @Autowired OrderService orderService;

    @GetMapping("/pending_delivery")
    public ResponseEntity<List<Order>> getPendingDeliveryOrders() {
        List<Order> orders = orderService.getOrdersByStatus("Chờ giao hàng");
        return ResponseEntity.ok(orders);
    }

    @PutMapping("start_delivery")
    public ResponseEntity<?> startDelivery(@RequestParam int orderID) {
        try {
            Order order = orderService.updateOrderStatus(orderID, "Đang giao hàng");
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update_order_status")
    public ResponseEntity<?> updateOrderStatus(@RequestParam int orderID,
                                               @RequestParam String status,
                                               @RequestParam(required = false) String reason) {
        try {
            Order order = orderService.updateOrderStatus(orderID, status, reason);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
