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
    private DiamondService diamondService;

    @Autowired
    private ProductCustomizeService productCustomizeService;

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

    @GetMapping("/orders_by_user/{id}")
    public ResponseEntity<?> getOrdersByUserID(@PathVariable int id) {
        try {
            List<Order> orders = orderService.getOrderByUserID(id);
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

            // Create Order details and calculate total price
            List<OrderDetail> orderDetails = new ArrayList<>();
            double totalPrice = 0.0;

            for (OrderDetailRequest detailRequest : orderRequest.getOrderDetail()) {
                // Handle diamondID
                if (detailRequest.getDiamondID() != null) {
                    OrderDetail diamondOrderDetail = new OrderDetail();
                    diamondOrderDetail.setOrder(order);
                    diamondOrderDetail.setQuantity(detailRequest.getQuantity());
                    diamondOrderDetail.setPrice(detailRequest.getPrice());

                    Diamond diamond = diamondService.getDiamondByDiamondID(detailRequest.getDiamondID());
                    if (diamond == null) {
                        return ResponseEntity.badRequest().body("Invalid diamond ID");
                    }
                    if (!diamond.isStatus()) {
                        return ResponseEntity.badRequest().body("Diamond is not available for order");
                    }
                    diamondOrderDetail.setDiamond(diamond);
                    orderDetails.add(diamondOrderDetail);

                    totalPrice += detailRequest.getPrice() * detailRequest.getQuantity();
                }

                // Handle productCustomizeID
                if (detailRequest.getProductCustomizeID() != null) {
                    OrderDetail productCustomizeOrderDetail = new OrderDetail();
                    productCustomizeOrderDetail.setOrder(order);
                    productCustomizeOrderDetail.setQuantity(detailRequest.getQuantity());
                    productCustomizeOrderDetail.setPrice(detailRequest.getPrice());

                    ProductCustomize productCustomize = productCustomizeService.
                            getProductCustomizeById(detailRequest.getProductCustomizeID());
                    if (productCustomize == null) {
                        return ResponseEntity.badRequest().body("Invalid product customize ID");
                    }
                    productCustomizeOrderDetail.setProductCustomize(productCustomize);
                    orderDetails.add(productCustomizeOrderDetail);

                    totalPrice += detailRequest.getPrice() * detailRequest.getQuantity();
                }
            }

            // Set the total price for the order
            order.setPrice(totalPrice);

            // Save Order
            Order savedOrder = orderService.saveOrder(order, orderDetails);
            if (savedOrder == null) {
                return ResponseEntity.badRequest().body("Error saving order");
            }

            return ResponseEntity.ok("Create Order successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

//    @PostMapping("/submit_order")
//    public ResponseEntity<?> submitOrder(@RequestBody OrderRequest orderRequest) {
//        try {
//            User user = userService.getUserByID(orderRequest.getUser().getUserID());
//            if (user == null) {
//                return ResponseEntity.badRequest().body("User does not exist");
//            }
//
//            // Create Order
//            Order order = new Order();
//            order.setAddressShipping(orderRequest.getAddressShipping());
//            order.setFullName(orderRequest.getFullName());
//            order.setOrderDate(orderRequest.getOrderDate());
//            order.setPhoneShipping(orderRequest.getPhoneShipping());
//            order.setPrice(orderRequest.getPrice());
//            order.setQuantity(orderRequest.getQuantity());
//            order.setStatus(orderRequest.getStatus());
//            order.setUser(user);
//
//            // Create Order details
//            List<OrderDetail> orderDetails = new ArrayList<>();
//            for (OrderDetailRequest detailRequest : orderRequest.getOrderDetail()) {
//                OrderDetail orderDetail = new OrderDetail();
//                orderDetail.setOrder(order);
//                orderDetail.setQuantity(detailRequest.getQuantity());
//                orderDetail.setPrice(detailRequest.getPrice());
//
//                if (detailRequest.getDiamondID() != null) {
//                    Diamond diamond = diamondService.getDiamondByDiamondID(detailRequest.getDiamondID());
//                    if (diamond == null) {
//                        return ResponseEntity.badRequest().body("Invalid diamond ID");
//                    }
//                    orderDetail.setDiamond(diamond);
//                } else if (detailRequest.getProductCustomizeID() != null) {
//                    ProductCustomize productCustomize = productCustomizeService.getProductCustomizeById(detailRequest.getProductCustomizeID());
//                    if (productCustomize == null) {
//                        return ResponseEntity.badRequest().body("Invalid product customize ID");
//                    }
//                    orderDetail.setProductCustomize(productCustomize);
//                } else {
//                    return ResponseEntity.badRequest().body("Order detail must have either a diamond or product ID");
//                }
//                orderDetails.add(orderDetail);
//            }
//
//            // Create payment
//            List<Payment> payments = new ArrayList<>();
//            for (PaymentRequest paymentRequest : orderRequest.getPayments()) {
//                Payment payment = new Payment();
//                payment.setPaymentAmount(paymentRequest.getPaymentAmount());
//                payment.setPaymentDate(paymentRequest.getPaymentDate());
//                payment.setRemainingAmount(paymentRequest.getRemainingAmount());
//                payment.setOrder(order);
//                payments.add(payment);
//            }
//
//            Order savedOrder = orderService.saveOrder(order, orderDetails, payments);
//            if (savedOrder == null) {
//                return ResponseEntity.badRequest().body("Error saving order");
//            }
//
//            return ResponseEntity.ok(savedOrder);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }
}
