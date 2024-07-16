package com.group6.swp391.controller;

import com.group6.swp391.model.*;
import com.group6.swp391.request.ConfirmOrderRequest;
import com.group6.swp391.request.OrderRequest;
import com.group6.swp391.response.NewOrderRespone;
import com.group6.swp391.response.OrderDetailRespone;
import com.group6.swp391.response.OrderRespone;
import com.group6.swp391.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/swp391/api/orders")
public class OrderController {
    @Autowired OrderService orderService;
    @Autowired UserService userService;
    @Autowired DiamondService diamondService;
    @Autowired CartService cartService;
    @Autowired CartItemService cartItemService;
    @Autowired OrderDetailService orderDetailService;
    @Autowired PointsService pointsService;
    @Autowired WarrantyCardService warrantyCardService;
    @Autowired ProductCustomizeService productCustomizeService;
    @Autowired ProductCustomizeServiceImp productCustomizeServiceImp;
    @Autowired OrderServiceImp orderServiceImp;

    @DeleteMapping("/delete_order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable int id, @RequestBody ConfirmOrderRequest confirmOrderRequest) {
        try {
            orderService.markOrderAsDeleted(id,confirmOrderRequest.getStatus(), confirmOrderRequest.getReason());
            return ResponseEntity.ok("Order deleted successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Method tinh tong so don hang moi voi trang thai dang doi xac nhan
     * @param args
     * @return number
     */
    @GetMapping("/orderpending")
    public ResponseEntity<Integer> countOrderPending() {
        try {
            List<Order> orders = orderService.getNewestOrder("Chờ xác nhận");
            if(orders == null || orders.isEmpty()) {
                return ResponseEntity.ok().body(0);
            }
            int count = orders.size();
            return ResponseEntity.ok().body(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }
    }

    /**
     * Method tinh tong so don hang voi trang thai dang doi thanh toan
     * @param args
     * @return number
     */
    @GetMapping("/orderWaitPay/{user_id}")
    public ResponseEntity<Integer> countOrderWaitPay(@PathVariable("user_id") int id) {
        try {
            User userExisting = userService.getUserByID(id);
            if(userExisting == null) {
                return ResponseEntity.badRequest().body(null);
            }
            List<Order> orders = orderService.getStatusByUser(userExisting.getUserID(), "Chờ thanh toán");
            if(orders == null || orders.isEmpty()) {
                return ResponseEntity.ok().body(0);
            }
            int count = orders.size();
            return ResponseEntity.ok().body(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }
    }

    /**
     * Method update cac trang thai order
     * dua vao trang thasi de thuc hien mot so quy trinh nhu tao diem,...
     * @param args
     * @return message success or fail
     */
    @PutMapping("/update_status/{order_id}")
    public ResponseEntity<?> updateStatusOrder(@PathVariable("order_id") int id, @RequestBody ConfirmOrderRequest confirmOrderRequest) {
        try {
            Order orderExisting = orderService.getOrderByOrderID(id);
            if (orderExisting == null) {
                return ResponseEntity.badRequest().body("Order Not Existing");
            }
            if(confirmOrderRequest.getStatus().equalsIgnoreCase("Chờ giao hàng")) {
                orderExisting.setStatus(confirmOrderRequest.getStatus());
                orderExisting.setReason(null);
                pointsService.createPoints(orderExisting.getUser().getUserID(), orderExisting.getOrderID());
            } else if(confirmOrderRequest.getStatus().equalsIgnoreCase("Đã giao")) {
                orderExisting.setStatus(confirmOrderRequest.getStatus());
                orderExisting.setReason(null);
                pointsService.createPoints(orderExisting.getUser().getUserID(), orderExisting.getOrderID());
                List<OrderDetail> orderDetails = orderExisting.getOrderDetails();
                for(OrderDetail orderDetail : orderDetails) {
                    WarrantyCard newWarrantyCard = new WarrantyCard();
                    User existingUser = userService.getUserByID(orderExisting.getUser().getUserID());
                    if(existingUser == null) {
                        return ResponseEntity.badRequest().body("User Not Null");
                    }
                    newWarrantyCard.setUser(existingUser);
                    newWarrantyCard.setOrder(orderExisting);
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.YEAR,2);
                    newWarrantyCard.setExpirationDate(calendar.getTime());
                    ProductCustomize productCustomize = orderDetail.getProductCustomize();
                    Diamond diamond = orderDetail.getDiamond();
                    if(orderDetail.getProductCustomize() != null) {
                        newWarrantyCard.setProductCustomize(productCustomize);
                        warrantyCardService.createNew(newWarrantyCard);
                    } else if(orderDetail.getDiamond() != null) {
                        newWarrantyCard.setDiamond(diamond);
                        warrantyCardService.createNew(newWarrantyCard);
                    }
                }
            } else if (confirmOrderRequest.getStatus().equalsIgnoreCase("Đã hủy")
                    || confirmOrderRequest.getStatus().equalsIgnoreCase("Hoàn Trả")
                    || confirmOrderRequest.getStatus().equalsIgnoreCase("Không Thành Công")) {
                orderExisting.setStatus(confirmOrderRequest.getStatus());
                orderExisting.setReason(confirmOrderRequest.getReason());
                for(OrderDetail orderDetail: orderExisting.getOrderDetails()) {
                    if(orderDetail.getProductCustomize() != null) {
                        orderServiceImp.incrementSizeQuantity(orderDetail.getProductCustomize(), orderDetail.getQuantity());
                    } else if(orderDetail.getDiamond() != null) {
                        diamondService.updateStatus(orderDetail.getDiamond().getDiamondID());
                    }
                }
            } else {
                orderExisting.setStatus(confirmOrderRequest.getStatus());
                orderExisting.setReason(null);
            }
            orderService.updateStatus(orderExisting.getOrderID(), orderExisting.getStatus(), orderExisting.getReason());
            return ResponseEntity.ok().body("Update Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Method get all order
     * @param args
     * @return list order
     */
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

    /**
     * Method get orderDetail
     * @param args orderid
     * @return order
     */
    //wait check format JSON,..
    @GetMapping("/{order_id}")
    public ResponseEntity<?> getOrdersById(@PathVariable("order_id") int id) {
        try {
            Order orderExisting = orderService.getOrderByOrderID(id);
            if(orderExisting == null) {
                return ResponseEntity.badRequest().body("Order not found with ID: " + id);
            }
            OrderDetailRespone orderDetailRespone = new OrderDetailRespone();
            orderDetailRespone.setOrderId(orderExisting.getOrderID());
            orderDetailRespone.setFullName(orderExisting.getFullName());
            orderDetailRespone.setEmail(orderExisting.getEmail());
            orderDetailRespone.setQuantity(orderExisting.getQuantity());
            orderDetailRespone.setPrice(orderExisting.getPrice());
            orderDetailRespone.setGender(orderExisting.getGender());
            orderDetailRespone.setPhoneShipping(orderExisting.getPhoneShipping());
            orderDetailRespone.setAddressShipping(orderExisting.getAddressShipping());
            orderDetailRespone.setPayments(orderExisting.getPayments());
            orderDetailRespone.setOrderDate(orderExisting.getOrderDate());
            orderDetailRespone.setOrderDetails(orderExisting.getOrderDetails());
            orderDetailRespone.setStatus(orderExisting.getStatus());
            orderDetailRespone.setReason(orderExisting.getReason());
            orderDetailRespone.setDeleteStatus(orderExisting.isDeleteStatus());
            orderDetailRespone.setNote(orderExisting.getNote());
            orderDetailRespone.setDiscount(orderExisting.getDiscount());
            return ResponseEntity.ok(orderDetailRespone);
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

    /**
     * Method get order by  user
     * @param args userId
     * @return List order
     */
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

    /**
     * Method get order with status cho xac nhan
     * @param args
     * @return list new order
     */
    @GetMapping("/newest_order")
    public ResponseEntity<?> getNewestOrder() {
        try {
            List<Order> newestOrders = orderService.getNewestOrder("Chờ xác nhận");
            if (newestOrders == null || newestOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No order found");
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

            return ResponseEntity.ok().body(newOrders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Method get order by status
     * @param args
     * @return list order by status
     */
    @GetMapping("/orders_by_status/{status}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable String status) {
        try {
            List<Order> newestOrders = orderService.getOrdersByStatus(status);
            if (newestOrders == null || newestOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No order found");
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

            return ResponseEntity.ok().body(newOrders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Method tao order moi
     * @param args orderRequest
     * @return message success or fail
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/submit_order")
    public ResponseEntity<?> submitOrder(@RequestBody @Valid OrderRequest orderRequest) {
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
                    if(!diamondInProductCustomize.isStatus()) {
                        return ResponseEntity.badRequest().body("Diamonds are out of stock");
                    }
                    diamondInProductCustomize.setStatus(false);
                    diamondService.saveDiamond(diamondInProductCustomize);
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
                pointsService.getUserPoints(orderRequest.getUserID(), newOrder.getOrderID(), orderRequest.getUsedPoint());
            }
            return ResponseEntity.ok().body("Create Order Successfull");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
