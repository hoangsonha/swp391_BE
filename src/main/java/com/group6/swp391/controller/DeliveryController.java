package com.group6.swp391.controller;

import com.group6.swp391.model.Order;
import com.group6.swp391.request.ResultRequest;
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

    @PutMapping("/order/{order_id}")
    public ResponseEntity<?> confirmGetOrder(@PathVariable("order_id") int id) {
        try {
            Order orderExisting = orderService.getOrderByOrderID(id);
            if(orderExisting == null) {
                return ResponseEntity.badRequest().body("No order found");
            }
            orderExisting.setStatus("confirm");
            orderService.updateStatus(orderExisting.getOrderID(), orderExisting.getStatus());
            return ResponseEntity.ok("Successfully confirmed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping("/order/result/{order_id}")
//    public ResponseEntity<?> confirmResult(@PathVariable("order_id") int id, @RequestBody ResultRequest request) {
//        try {
//            Order orderExisting = orderService.getOrderByOrderID(id);
//            if(orderExisting == null) {
//                return ResponseEntity.badRequest().body("No order found");
//            }
//            if(request.getStatus().equalsIgnoreCase("successfull")) {
//                orderExisting.setStatus("successfull");
//            } else if(request.getStatus().equalsIgnoreCase("failed")) {
//                orderExisting.setStatus("Failed");
//                orderExisting.setReason(request.getMessage());
//            }
//            orderService.save(orderExisting);
//            return ResponseEntity.ok().body("Successfully confirmed order");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }


}
