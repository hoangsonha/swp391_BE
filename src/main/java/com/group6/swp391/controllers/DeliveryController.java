package com.group6.swp391.controllers;

import com.group6.swp391.enums.EnumOrderStatus;
import com.group6.swp391.pojos.Order;
import com.group6.swp391.responses.NewOrderRespone;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.responses.OrderRespone;
import com.group6.swp391.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/swp391/api/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {

    @Autowired OrderService orderService;

    // Method to get all pending delivery orders
    // Returns a list of orders with status "Chờ giao hàng"
    @PreAuthorize("hasRole('DELIVERY')")
    @GetMapping("/pending_delivery")
    public ResponseEntity<ObjectResponse> getPendingDeliveryOrders() {
        try {
            List<Order> orders = orderService.getOrdersByStatus(EnumOrderStatus.Chờ_giao_hàng.name().replaceAll("_", " "));
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ObjectResponse("Failed", "Danh sách đơn hàng chờ giao rỗng", null));
            } else {
                return ResponseEntity.ok(new ObjectResponse("Success", "Lấy danh sách đơn hàng chờ giao thành công", orders));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse("Failed", "Lấy danh sách đơn hàng chờ giao không thành công", null));
        }
    }

    // Method to start delivery of an order
    // Accepts an order ID as a request parameter and updates the order status to "Đang giao hàng"
    @PreAuthorize("hasRole('DELIVERY')")
    @PutMapping("/start_delivery")
    public ResponseEntity<ObjectResponse> startDelivery(@RequestParam int orderID) {
        try {
            Order order = orderService.updateOrderStatus(orderID, "Đang giao hàng");
            return ResponseEntity.ok(new ObjectResponse("Success", "Cập nhật trạng thái đơn hàng thành công", order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ObjectResponse("Failed", "Cập nhật trạng thái đơn hàng không thành công", e.getMessage()));
        }
    }

    // Method to update the status of an order
    // Accepts an order ID, status, and optional reason as request parameters, and updates the order status
    @PreAuthorize("hasRole('DELIVERY')")
    @PutMapping("/update_order_status")
    public ResponseEntity<ObjectResponse> updateOrderStatus(@RequestParam int orderID,
                                                            @RequestParam String status,
                                                            @RequestParam(required = false) String reason) {
        try {
            Order order = orderService.updateOrderStatus(orderID, status, reason);
            return ResponseEntity.ok(new ObjectResponse("Success", "Cập nhật trạng thái đơn hàng thành công", order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ObjectResponse("Failed", "Cập nhật trạng thái đơn hàng không thành công", e.getMessage()));
        }
    }

    // Method to get the newest orders with status "Chờ giao hàng"
    // Groups orders by user and returns a list of NewOrderRespone objects
    @PreAuthorize("hasRole('DELIVERY')")
    @GetMapping("/newest_order/{delivery_id}")
    public ResponseEntity<ObjectResponse> getNewestOrder(@PathVariable("delivery_id") int id) {
        try {
            List<Order> newestOrders = orderService.getNewestOrderDelivery(id, EnumOrderStatus.Chờ_giao_hàng.name().replaceAll("_", " "));
            if (newestOrders == null || newestOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ObjectResponse("Success", "Không tìm thấy đơn hàng", null));
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

            return ResponseEntity.ok(new ObjectResponse("Success", "Lấy danh sách đơn hàng mới nhất thành công", newOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ObjectResponse("Failed", "Lấy danh sách đơn hàng mới nhất không thành công", e.getMessage()));
        }
    }

    // Method to get the count of pending delivery orders
    // Returns the count of orders with status "Chờ giao hàng"
    @PreAuthorize("hasRole('DELIVERY')")
    @GetMapping("/pending_delivery_count/{delivery_id}")
    public ResponseEntity<ObjectResponse> getPendingDeliveryOrderCount(@PathVariable("delivery_id") int deliveryId) {
        try {
            List<Order> list = orderService.getNewestOrderDelivery(deliveryId, EnumOrderStatus.Chờ_giao_hàng.name().replaceAll("_", " "));
            if(list == null || list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Số lượng đơn hàng cần giao rỗng", null));
            }
            Integer count = list.size();
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Số lượng đơn hàng cần giao", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Success", "Message: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('DELIVERY')")
    @GetMapping("/all_by_delivery/{delivery_id}")
    public ResponseEntity<ObjectResponse> getAllStaff(@PathVariable("delivery_id") int deliveryId) {
        try {
            List<Order> listOrderWithStaff = orderService.getAllWithStaff(deliveryId);
            if(listOrderWithStaff == null || listOrderWithStaff.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed","Danh sách đơn hàng rỗng", null));
            }
            listOrderWithStaff.sort(Comparator.comparing(Order::getOrderDate).reversed());
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Danh sách đơn hàng", listOrderWithStaff));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed","Message" + e.getMessage(), null));
        }
    }

}
