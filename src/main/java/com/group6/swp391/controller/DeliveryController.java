package com.group6.swp391.controller;

import com.group6.swp391.model.Order;
import com.group6.swp391.response.NewOrderRespone;
import com.group6.swp391.response.OrderRespone;
import com.group6.swp391.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/swp391/api/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {

    @Autowired OrderService orderService;

    @PreAuthorize("hasRole('DELIVERY')")
    @GetMapping("/pending_delivery")
    public ResponseEntity<List<Order>> getPendingDeliveryOrders() {
        List<Order> orders = orderService.getOrdersByStatus("Chờ giao hàng");
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasRole('DELIVERY')")
    @PutMapping("/start_delivery")
    public ResponseEntity<?> startDelivery(@RequestParam int orderID) {
        try {
            Order order = orderService.updateOrderStatus(orderID, "Đang giao hàng");
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('DELIVERY')")
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

    @PreAuthorize("hasRole('DELIVERY')")
    @GetMapping("/newest_order")
    public ResponseEntity<?> getNewestOrder() {
        try {
            List<Order> newestOrders = orderService.getNewestOrder("Chờ giao hàng");
            if (newestOrders == null || newestOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No order found");
            }

            Map<Integer, List<OrderRespone>> userOrdersMap = new HashMap<>();

            for (Order order : newestOrders) {
                int userId = order.getUser().getUserID();
                OrderRespone orderRespone = new OrderRespone();
                orderRespone.setOrderId(order.getOrderID());
                orderRespone.setDiscount(order.getDiscount());
                orderRespone.setOrderDate(order.getOrderDate());
                orderRespone.setQuantity(order.getQuantity());
                orderRespone.setPrice(order.getPrice());
                orderRespone.setStatus(order.getStatus());
                orderRespone.setOrderDetail(order.getOrderDetails().get(0));
                userOrdersMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(orderRespone);
            }

            List<NewOrderRespone> newOrders = new ArrayList<>();
            for (Map.Entry<Integer, List<OrderRespone>> entry : userOrdersMap.entrySet()) {
                NewOrderRespone newOrderRespone = new NewOrderRespone();
                newOrderRespone.setUserId(entry.getKey());
                newOrderRespone.setOrders(entry.getValue());
                newOrders.add(newOrderRespone);
            }

            return ResponseEntity.ok().body(newOrders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('DELIVERY')")
    @GetMapping("/pending_delivery_count")
    public ResponseEntity<Long> getPendingDeliveryOrderCount() {
        try {
            long count = orderService.getPendingDeliveryOrderCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0L);
        }
    }

}
