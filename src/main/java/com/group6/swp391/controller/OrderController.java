package com.group6.swp391.controller;

import com.group6.swp391.model.*;
import com.group6.swp391.request.ConfirmOrderRequest;
import com.group6.swp391.request.OrderRequest;
import com.group6.swp391.response.NewOrderRespone;
import com.group6.swp391.response.OrderRespone;
import com.group6.swp391.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    PointsServiceImp pointsServiceImp;

    @Autowired
    private CartService cartService;

    @Autowired CartItemService cartItemService;

    @Autowired
    private OrderDetailService orderDetailService;

    @DeleteMapping("/delete_order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable int id, @RequestBody ConfirmOrderRequest confirmOrderRequest) {
        try {
            orderService.markOrderAsDeleted(id,confirmOrderRequest.getStatus(), confirmOrderRequest.getReason());
            return ResponseEntity.ok("Order deleted successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/orderpending/{user_id}")
    public ResponseEntity<Integer> countOrderPending(@PathVariable("user_id") int id) {
        try {
            User userExisting = userService.getUserByID(id);
            if(userExisting == null) {
                return ResponseEntity.badRequest().body(0);
            }
            List<Order> orders = orderService.getStatusByUser(userExisting.getUserID(), "Chờ xác nhận");
            int count = orders.size();
            return ResponseEntity.ok().body(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0);
        }
    }

    @PutMapping("/update_status/{order_id}")
    public ResponseEntity<?> updateStatusOrder(@PathVariable("order_id") int id, @RequestBody ConfirmOrderRequest confirmOrderRequest) {
        try {
            Order orderExisting = orderService.getOrderByOrderID(id);
            if (orderExisting == null) {
                return ResponseEntity.badRequest().body("Order Not Existing");
            }
            orderExisting.setStatus(confirmOrderRequest.getStatus());
            if(confirmOrderRequest.getReason() != null) {
                orderExisting.setReason(confirmOrderRequest.getReason());
            }
            orderService.updateStatus(orderExisting.getOrderID(), orderExisting.getStatus(), orderExisting.getReason());
            return ResponseEntity.ok().body("Update Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //wait check format JSON,..
    @GetMapping("/all_orders")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrder());
    }

    //wait check format JSON,..
    @GetMapping("/orders_by_diamond/{id}")
    public ResponseEntity<?> getOrdersByDiamondID(@PathVariable String id) {
        try {
            List<Order> orders = orderService.getOrderByDiamondID(id);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    //wait check format JSON,..
    @GetMapping("/{order_id}")
    public ResponseEntity<?> getOrdersById(@PathVariable("order_id") int id) {
        try {
            Order orderExisting = orderService.getOrderByOrderID(id);
            if(orderExisting == null) {
                return ResponseEntity.badRequest().body("Order not found with ID: " + id);
            }
            return ResponseEntity.ok(orderExisting);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //wait check format JSON,..
    @GetMapping("/order_details_by_order/{id}")
    public ResponseEntity<?> getOrderDetailsByOrderID(@PathVariable int id) {
        try {
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderID(id);
            if (orderDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No order details found for order ID: " + id);
            }
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving order details: " + e.getMessage());
        }
    }

    @GetMapping("/orders_by_user/{id}")
    public ResponseEntity<?> getOrdersByUserID(@PathVariable int id) {
        try {
            List<Order> orders = orderService.getOrderByUserID(id);
            if(orders == null) {
                return ResponseEntity.badRequest().body("Order not found with ID: " + id);
            }
            List<NewOrderRespone> newOrders = new ArrayList<>();
            List<OrderRespone> orderRespones = new ArrayList<>();
            NewOrderRespone newOrderRespone = new NewOrderRespone();
            for (Order order : orders) {
                newOrderRespone.setUserId(order.getUser().getUserID());
                OrderRespone orderRespone = new OrderRespone();
                orderRespone.setOrderId(order.getOrderID());
                orderRespone.setDiscount(order.getDiscount());
                orderRespone.setOrderDate(order.getOrderDate());
                orderRespone.setQuantity(order.getQuantity());
                orderRespone.setPrice(order.getPrice());
                orderRespone.setStatus(order.getStatus());
                orderRespone.setOrderDetail(order.getOrderDetails().get(0));
                orderRespones.add(orderRespone);
                newOrderRespone.setOrders(orderRespones);
            }
            newOrders.add(newOrderRespone);
            return ResponseEntity.ok(newOrderRespone);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/newest_order")
    public ResponseEntity<?> getNewestOrder() {
        try {
            List<Order> newestOrder = orderService.getNewestOrder("Chờ xác nhận");
            if (newestOrder == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No order found");
            }
            List<NewOrderRespone> newOrders = new ArrayList<>();
            for (Order order : newestOrder) {
                NewOrderRespone newOrderRespone = new NewOrderRespone();
                newOrderRespone.setUserId(order.getUser().getUserID());
                List<OrderRespone> orderRespones = new ArrayList<>();
                OrderRespone orderRespone = new OrderRespone();
                orderRespone.setOrderId(order.getOrderID());
                orderRespone.setDiscount(order.getDiscount());
                orderRespone.setOrderDate(order.getOrderDate());
                orderRespone.setQuantity(order.getQuantity());
                orderRespone.setPrice(order.getPrice());
                orderRespone.setStatus(order.getStatus());
                orderRespone.setOrderDetail(order.getOrderDetails().get(0));
                orderRespones.add(orderRespone);
                newOrderRespone.setOrders(orderRespones);
                newOrders.add(newOrderRespone);
            }
            return ResponseEntity.ok().body(newOrders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/orders_by_status/{status}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable String status) {
        try {
            List<Order> orders = orderService.getOrdersByStatus(status);
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No orders found with status: " + status);
            }
            List<NewOrderRespone> newOrders = new ArrayList<>();
            List<OrderRespone> orderRespones = new ArrayList<>();
            NewOrderRespone newOrderRespone = new NewOrderRespone();
            for (Order order : orders) {
                newOrderRespone.setUserId(order.getUser().getUserID());
                OrderRespone orderRespone = new OrderRespone();
                orderRespone.setOrderId(order.getOrderID());
                orderRespone.setOrderDetail(order.getOrderDetails().get(0));
                orderRespones.add(orderRespone);
                newOrderRespone.setOrders(orderRespones);
            }
            newOrders.add(newOrderRespone);
            return ResponseEntity.ok().body(newOrders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/submit_order")
    public ResponseEntity<?> submitOrder(@RequestBody OrderRequest orderRequest) {
        try {
            User existingUser = userService.getUserByID(orderRequest.getUserID());
            if(existingUser == null) {
                return ResponseEntity.badRequest().body("User not found");
            }
            Order newOrder = new Order();
            newOrder.setUser(existingUser);
            newOrder.setFullName(orderRequest.getFullName());
            newOrder.setPhoneShipping(orderRequest.getPhoneShipping());
            newOrder.setAddressShipping(orderRequest.getAddressShipping());
            newOrder.setGender(orderRequest.getGender());
            newOrder.setNote(orderRequest.getNote());
            newOrder.setEmail(orderRequest.getEmail());
            newOrder.setStatus("Chờ xác nhận");
            newOrder.setPrice(orderRequest.getPrice());
            newOrder.setDiscount(orderRequest.getDiscount());
            Cart existingCart = cartService.getCart(existingUser.getUserID());
            if(existingCart == null) {
                return ResponseEntity.badRequest().body("Cart not found");
            }
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (CartItem cartItem : existingCart.getItems()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(newOrder);
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setPrice(cartItem.getTotalPrice());
                if(cartItem.getDiamondAdd() != null) {
                    Diamond diamond = cartItem.getDiamondAdd();
                    if(!diamond.isStatus()) {
                        return ResponseEntity.badRequest().body("Diamond is not available for order");
                    }
                    diamond.setStatus(false);
                    diamondService.saveDiamond(diamond);
                    orderDetail.setDiamond(diamond);
                }
                if (cartItem.getProductCustomize() != null) {
                    ProductCustomize productCustomize = cartItem.getProductCustomize();
                    Diamond diamondInProductCustomize = productCustomize.getDiamond();
                    if (diamondInProductCustomize != null) {
                        diamondInProductCustomize.setStatus(false);
                        diamondService.saveDiamond(diamondInProductCustomize);
                    }
                    orderDetail.setProductCustomize(productCustomize);
                }
                newOrder.setQuantity(newOrder.getQuantity() + orderDetail.getQuantity());
                orderDetails.add(orderDetail);
            }
            if (orderDetails.isEmpty() || orderDetails == null) {
                return ResponseEntity.badRequest().body("Product in order details Empty");
            }
            orderService.saveOrder(newOrder, orderDetails);
            cartService.clearCart(newOrder.getUser().getUserID());
            if(orderRequest.getUsedPoint() > 0.0) {
                pointsServiceImp.getUserPoints(orderRequest.getUserID(), newOrder.getOrderID(), orderRequest.getUsedPoint());
            }
            return ResponseEntity.ok().body("Create Order Successfull");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
