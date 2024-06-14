package com.group6.swp391.controller;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.OrderDetail;
import com.group6.swp391.model.Payment;
import com.group6.swp391.request.OrderDetailRequest;
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
            if (orderService.getOrderByOrderID(orderRequest.getOrderID()) != null) {
                return ResponseEntity.badRequest().body("Order ID already exists");
            }
            if (userService.getUserByID(orderRequest.getUser().getUserID()) == null) {
                return ResponseEntity.badRequest().body("User does not exist");
            }
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
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (OrderDetailRequest detailRequest : orderRequest.getOrderDetail()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(newOrder);
                orderDetail.setQuantity(detailRequest.getQuantity());
                orderDetail.setPrice(detailRequest.getPrice());
                orderDetail.setDiamond(detailRequest.getDiamond());
                orderDetail.setProductCustomize(detailRequest.getProductCustomize());
                orderDetails.add(orderDetail);
            }
            orderService.createOrderDetails(orderDetails);
            newOrder.setOrderDetails(orderDetails);
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

        List<OrderDetail> existingDetails = order.getOrderDetails();
        existingDetails.clear();

        for (OrderDetailRequest detailRequest : orderRequest.getOrderDetail()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProductCustomize(detailRequest.getProductCustomize());
            detail.setDiamond(detailRequest.getDiamond());
            detail.setQuantity(detailRequest.getQuantity());
            detail.setPrice(detailRequest.getPrice());
            existingDetails.add(detail);
        }
        orderService.save(order);
        return ResponseEntity.ok("Order updated successfully with ID: " + order.getOrderID());
    }

    @DeleteMapping("/delete_order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable int id) {
        try {
            orderService.markOrderAsDeleted(id);
            return ResponseEntity.ok("Order deleted successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/all_orders")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrder());
    }

    @GetMapping("/orders_by_diamond/{id}")
    public ResponseEntity<?> getOrdersByDiamondID(@PathVariable String id) {
        try {
            List<Order> orders = orderService.getOrderByDiamondID(id);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
