package com.group6.swp391.controller;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.Payment;
import com.group6.swp391.request.OrderRequest;
import com.group6.swp391.service.OrderService;
import com.group6.swp391.service.PaymentService;
import com.group6.swp391.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/swp391/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create_order")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        Order newOrder;
        try {
            // Check if orderID exists
//            if (orderService.getOrderByOrderID(orderRequest.getOrderID())) {
//                return ResponseEntity.badRequest().body("Order ID already exists");
//            }
            // Check if the user exists
            if (userService.getUserByID(orderRequest.getUser().getUserID()) == null) {
                return ResponseEntity.badRequest().body("User does not exist");
            }

            // Create the Order
            Order order = new Order();
            order.setAddressShipping(orderRequest.getAddressShipping());
            order.setFullName(orderRequest.getFullName());
            order.setOrderDate(orderRequest.getOrderDate());
            order.setPhoneShipping(orderRequest.getPhoneShipping());
            order.setPrice(orderRequest.getPrice());
            order.setQuantity(orderRequest.getQuantity());
            order.setStatus(orderRequest.getStatus());
            order.setUser(orderRequest.getUser());

            newOrder = orderService.createOrder(order);

            List<Payment> paymentsToSave = new ArrayList<>();
            for (Payment payment : orderRequest.getPayments()) {
                payment.setOrder(newOrder);
                paymentsToSave.add(payment);
            }

            paymentService.createPayments(paymentsToSave);

            newOrder.setPayments(paymentsToSave);

            return ResponseEntity.ok("Order created successfully with ID: " + newOrder.getOrderID());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update_order/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable int id, @RequestBody OrderRequest orderRequest) {
        Order order = orderService.getOrderByOrderID(id);
        if (order == null) {
            return ResponseEntity.badRequest().body("Order not found");
        }

        order.setAddressShipping(orderRequest.getAddressShipping());
        order.setFullName(orderRequest.getFullName());
        order.setOrderDate(orderRequest.getOrderDate());
        order.setPhoneShipping(orderRequest.getPhoneShipping());
        order.setPrice(orderRequest.getPrice());
        order.setQuantity(orderRequest.getQuantity());
        order.setStatus(orderRequest.getStatus());
        order.setUser(orderRequest.getUser());
        order.setPayments(orderRequest.getPayments());

        Order updatedOrder = orderService.save(order);
        return ResponseEntity.ok("Order updated successfully with ID: " + updatedOrder.getOrderID());
    }

//    @DeleteMapping("/delete_order/{id}")
//    public ResponseEntity<?> deleteOrder(@PathVariable int id) {
//        Order order = orderService.getOrderByOrderID(id);
//        if (order == null) {
//            return ResponseEntity.badRequest().body("Order not found");
//        }
//    }

    @GetMapping("/all_orders")
    public ResponseEntity<?> getAllOrders() {
        List<Order> orders;
        try {
            orders = orderService.getAllOrder();
            if  (orders == null) {
                return ResponseEntity.badRequest().body("Order list is not available");
            } else {
                return ResponseEntity.ok(orders);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get all orders failed");
        }
    }
}
