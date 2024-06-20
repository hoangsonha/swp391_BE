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

    @GetMapping("/order")
    public ResponseEntity<?> getOrderByStatus() {
        List<Order> orders = null;
        try {
            orders = orderService.getOrdersByStatus("paymented");
            if(orders == null) {
                return ResponseEntity.badRequest().body("No orders found");
            }
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



//    public ResponseEntity<?> ConfirmOrder(@PathVariable("order_id") int id, @RequestBody String status) {
//        try {
//            Order orderExisting = orderService.getOrderByOrderID(id);
//            if(orderExisting == null) {
//                return ResponseEntity.badRequest().body("No order found");
//            }
//            if(status.equalsIgnoreCase("successfull")) {
//
//            }
//            orderExisting.setStatus(status);
//        }
//    }


}
