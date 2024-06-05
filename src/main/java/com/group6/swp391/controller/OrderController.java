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

//    @PostMapping("/create_order")
//    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
//        Order newOrder;
//        try {
//            // Check if orderID exists
//
//            // Check if the user exists
//
//            Order order = new Order();
//            order.setAddressShipping(orderRequest.getAddressShipping());
//            order.setFullName(orderRequest.getFullName());
//            order.setOrderDate(orderRequest.getOrderDate());
//            order.setPhoneShipping(orderRequest.getPhoneShipping());
//            order.setPrice(orderRequest.getPrice());
//            order.setQuantity(orderRequest.getQuantity());
//            order.setStatus(orderRequest.getStatus());
//            order.setUser(orderRequest.getUser());
//
//            newOrder = orderService.createOrder(order);
//
//            List<Payment> paymentsToSave = new ArrayList<>();
//            for (Payment payment : orderRequest.getPayments()) {
//                payment.setOrder(newOrder);
//                paymentsToSave.add(payment);
//            }
//            //paymentService.createPayment(paymentsToSave);
//
//            return ResponseEntity.ok("Order created successfully with ID: ");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }

    @GetMapping("/all_orders")
    public ResponseEntity<?> getAllOrders() {
        List<Order> orders;
        try {
            orders = orderService.getAllOrder();
            if  (orders == null) {
                throw new RuntimeException("Order list is empty");
            } else {
                return ResponseEntity.ok(orders);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Get all orders failed");
        }
    }
}
