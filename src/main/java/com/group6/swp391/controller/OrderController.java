package com.group6.swp391.controller;

import com.group6.swp391.model.*;
import com.group6.swp391.request.OrderDetailRequest;
import com.group6.swp391.request.OrderRequest;
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
    private ProductCustomizeService productCustomizeService;

    @Autowired
    PointsServiceImp pointsServiceImp;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

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

    @GetMapping("/orders_by_user/{id}")
    public ResponseEntity<?> getOrdersByUserID(@PathVariable int id) {
        try {
            List<Order> orders = orderService.getOrderByUserID(id);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/newest_order")
    public ResponseEntity<?> getNewestOrder() {
        try {
            List<Order> newestOrder = orderService.getNewestOrder("pendding");
            if (newestOrder == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No order found");
            }
            return ResponseEntity.ok(newestOrder);
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
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/submit_order")
    public ResponseEntity<?> submitOrder(@RequestBody OrderRequest orderRequest) {
        try {
            User user = userService.getUserByID(orderRequest.getUserID());
            if (user == null) {
                return ResponseEntity.badRequest().body("User does not exist");
            }

            // Retrieve the user's cart and cart items
            Cart cart = cartService.getCart(user.getUserID());
            if (cart == null) {
                return ResponseEntity.badRequest().body("Cart does not exist");
            }

            List<CartItem> cartItems = cartItemService.getCartItemsByCartID(cart.getCartId());
            if (cartItems == null) {
                return ResponseEntity.badRequest().body("Cart is empty");
            }

            // Create Order
            Order order = new Order();
            order.setAddressShipping(orderRequest.getAddressShipping());
            order.setFullName(orderRequest.getFullName());
            order.setOrderDate(orderRequest.getOrderDate());
            order.setPhoneShipping(orderRequest.getPhoneShipping());
            order.setPrice(orderRequest.getPrice());
            order.setQuantity(orderRequest.getQuantity());
            order.setStatus("Pending");
            order.setUser(user);

            // Create Order details and calculate total price
            List<OrderDetail> orderDetails = new ArrayList<>();
            double totalPrice = 0.0;

            for (OrderDetailRequest detailRequest : orderRequest.getOrderDetail()) {
                // Handle diamondID
                if (detailRequest.getDiamondID() != null) {
                    Diamond diamond = diamondService.getDiamondByDiamondID(detailRequest.getDiamondID());
                    if (diamond == null) {
                        return ResponseEntity.badRequest().body("Invalid diamond ID");
                    }
                    if (!diamond.isStatus()) {
                        return ResponseEntity.badRequest().body("Diamond is not available for order");
                    }

                    OrderDetail diamondOrderDetail = new OrderDetail();
                    diamondOrderDetail.setOrder(order);
                    diamondOrderDetail.setQuantity(detailRequest.getQuantity());
                    diamondOrderDetail.setPrice(detailRequest.getPrice());

                    // Update diamond status to false
                    diamond.setStatus(false);
                    // Save the updated diamond
                    diamondService.saveDiamond(diamond);

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

                    ProductCustomize productCustomize = productCustomizeService
                            .getProductCustomizeById(detailRequest.getProductCustomizeID());
                    if (productCustomize == null) {
                        return ResponseEntity.badRequest().body("Invalid product customize ID");
                    }

                    // Update diamond status to false in ProductCustomize
                    Diamond diamondInProductCustomize = productCustomize.getDiamond();
                    if (diamondInProductCustomize != null) {
                        diamondInProductCustomize.setStatus(false);
                        diamondService.saveDiamond(diamondInProductCustomize);
                    }

                    productCustomizeOrderDetail.setProductCustomize(productCustomize);
                    orderDetails.add(productCustomizeOrderDetail);

                    totalPrice += detailRequest.getPrice() * detailRequest.getQuantity();
                }
            }

            // Set the total price for the order
            // order.setPrice(totalPrice);
            order.setPrice(orderRequest.getPrice());

            // Save the Order and order details
            Order savedOrder = orderService.saveOrder(order, orderDetails);

            // Clear the cart item
            cartItemService.deleteAllByCart(cart);

            if (savedOrder == null) {
                return ResponseEntity.badRequest().body("Error saving order");
            }
            pointsServiceImp.createPoints(savedOrder.getUser().getUserID(), savedOrder.getOrderID(), orderRequest.getUsedPoint());

            return ResponseEntity.ok("Create Order successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
