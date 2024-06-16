package com.group6.swp391.controller;

import com.group6.swp391.model.*;
import com.group6.swp391.request.OrderDetailRequest;
import com.group6.swp391.request.OrderRequest;
import com.group6.swp391.request.PaymentRequest;
import com.group6.swp391.service.*;
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

    @Autowired
    private DiamondService diamondService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCustomizeService productCustomizeService;

//    @PostMapping("/create_order")
//    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
//        Order newOrder;
//        try {
//            if (orderService.getOrderByOrderID(orderRequest.getOrderID()) != null) {
//                return ResponseEntity.badRequest().body("Order ID already exists");
//            }
//            if (userService.getUserByID(orderRequest.getUser().getUserID()) == null) {
//                return ResponseEntity.badRequest().body("User does not exist");
//            }
//            Order order = new Order();
//            order.setAddressShipping(orderRequest.getAddressShipping());
//            order.setFullName(orderRequest.getFullName());
//            order.setOrderDate(orderRequest.getOrderDate());
//            order.setPhoneShipping(orderRequest.getPhoneShipping());
//            order.setPrice(orderRequest.getPrice());
//            order.setQuantity(orderRequest.getQuantity());
//            order.setStatus(orderRequest.getStatus());
//            order.setUser(orderRequest.getUser());
//            newOrder = orderService.createOrder(order);
//            List<Payment> paymentsToSave = new ArrayList<>();
//            for (Payment payment : orderRequest.getPayments()) {
//                payment.setOrder(newOrder);
//                paymentsToSave.add(payment);
//            }
//            paymentService.createPayments(paymentsToSave);
//            newOrder.setPayments(paymentsToSave);
//            List<OrderDetail> orderDetails = new ArrayList<>();
//            for (OrderDetailRequest detailRequest : orderRequest.getOrderDetail()) {
//                OrderDetail orderDetail = new OrderDetail();
//                orderDetail.setOrder(newOrder);
//                orderDetail.setQuantity(detailRequest.getQuantity());
//                orderDetail.setPrice(detailRequest.getPrice());
//                orderDetail.setDiamond(detailRequest.getDiamond());
//                orderDetail.setProductCustomize(detailRequest.getProductCustomize());
//                orderDetails.add(orderDetail);
//            }
//            orderService.createOrderDetails(orderDetails);
//            newOrder.setOrderDetails(orderDetails);
//            return ResponseEntity.ok("Order created successfully with ID: " + newOrder.getOrderID());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }

//    @PutMapping("/update_order/{id}")
//    public ResponseEntity<?> updateOrder(@PathVariable int id, @RequestBody OrderRequest orderRequest) {
//        Order order = orderService.getOrderByOrderID(id);
//        if (order == null) {
//            return ResponseEntity.badRequest().body("Order not found");
//        }
//        order.setAddressShipping(orderRequest.getAddressShipping());
//        order.setFullName(orderRequest.getFullName());
//        order.setOrderDate(orderRequest.getOrderDate());
//        order.setPhoneShipping(orderRequest.getPhoneShipping());
//        order.setPrice(orderRequest.getPrice());
//        order.setQuantity(orderRequest.getQuantity());
//        order.setStatus(orderRequest.getStatus());
//        order.setUser(orderRequest.getUser());
//
//        List<OrderDetail> existingDetails = order.getOrderDetails();
//        existingDetails.clear();
//
//        for (OrderDetailRequest detailRequest : orderRequest.getOrderDetail()) {
//            OrderDetail detail = new OrderDetail();
//            detail.setOrder(order);
//            detail.setProductCustomize(detailRequest.getProductCustomize());
//            detail.setDiamond(detailRequest.getDiamond());
//            detail.setQuantity(detailRequest.getQuantity());
//            detail.setPrice(detailRequest.getPrice());
//            existingDetails.add(detail);
//        }
//        orderService.save(order);
//        return ResponseEntity.ok("Order updated successfully with ID: " + order.getOrderID());
//    }

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

    @PostMapping("/submit_order")
    public ResponseEntity<?> submitOrder(@RequestBody OrderRequest orderRequest) {
        try {
            User user = userService.getUserByID(orderRequest.getUser().getUserID());
            if (user == null) {
                return ResponseEntity.badRequest().body("User does not exist");
            }

            // Create Order
            Order order = new Order();
            order.setAddressShipping(orderRequest.getAddressShipping());
            order.setFullName(orderRequest.getFullName());
            order.setOrderDate(orderRequest.getOrderDate());
            order.setPhoneShipping(orderRequest.getPhoneShipping());
            order.setPrice(orderRequest.getPrice());
            order.setQuantity(orderRequest.getQuantity());
            order.setStatus(orderRequest.getStatus());
            order.setUser(user);

            // Create Order details
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (OrderDetailRequest detailRequest : orderRequest.getOrderDetail()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setQuantity(detailRequest.getQuantity());
                orderDetail.setPrice(detailRequest.getPrice());

                if (detailRequest.getDiamond().getDiamondID() != null) {
                    Diamond diamond = diamondService.getDiamondByDiamondID(detailRequest.getDiamond().getDiamondID());
                    if (diamond == null) {
                        return ResponseEntity.badRequest().body("Invalid diamond ID");
                    }
                    orderDetail.setDiamond(diamond);
                } else if (detailRequest.getProductCustomize().getProdcutCustomId() != null) {
                    ProductCustomize productCustomize = productCustomizeService.getProductCustomizeById(detailRequest
                            .getProductCustomize().getProdcutCustomId());
                    if (productCustomize == null) {
                        return ResponseEntity.badRequest().body("Invalid product ID");
                    }
                    orderDetail.setProductCustomize(productCustomize);
                } else {
                    return ResponseEntity.badRequest().body("Order detail must have either a diamond or product ID");
                }
                orderDetails.add(orderDetail);
            }

            // Create payment
            List<Payment> payments = new ArrayList<>();
            for (PaymentRequest paymentRequest : orderRequest.getPayments()) {
                Payment payment = new Payment();
                payment.setPaymentAmount(paymentRequest.getPaymentAmount());
                payment.setPaymentDate(paymentRequest.getPaymentDate());
                payment.setRemainingAmount(paymentRequest.getRemainingAmount());
                payment.setOrder(order);
                payments.add(payment);
            }

            Order savedOrder = orderService.saveOrder(order, orderDetails, payments);
            if (savedOrder == null) {
                return ResponseEntity.badRequest().body("Error saving order");
            }

            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
