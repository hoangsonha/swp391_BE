package com.group6.swp391.controllers;

import com.group6.swp391.enums.EnumOrderStatus;
import com.group6.swp391.pojos.*;
import com.group6.swp391.requests.ConfirmOrderRequest;
import com.group6.swp391.requests.OrderRequest;
import com.group6.swp391.responses.NewOrderRespone;
import com.group6.swp391.responses.ObjectResponse;
import com.group6.swp391.responses.OrderDetailRespone;
import com.group6.swp391.responses.OrderRespone;
import com.group6.swp391.services.*;
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
    @Autowired PointsService pointsService;
    @Autowired WarrantyCardService warrantyCardService;
    @Autowired ProductCustomizeService productCustomizeService;
    @Autowired ProductCustomizeServiceImp productCustomizeServiceImp;
    @Autowired OrderServiceImp orderServiceImp;


    /**
     * Method tinh tong so don hang moi voi trang thai dang doi xac nhan
     * @return number
     */
    @PreAuthorize("hasRole('USER') or hasRole('STAFF')")
    @GetMapping("/orderpending/{staff_id}")
    public ResponseEntity<ObjectResponse> countOrderPending(@PathVariable("staff_id") int staffId) {
        try {
            List<Order> orders = orderService.getNewestOrderStaff(staffId,EnumOrderStatus.Chờ_xác_nhận.name().replaceAll("_"," "));
            if(orders == null || orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Không có đơn hàng mới", null));
            }
            int count = orders.size();
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Số lượng đơn hàng mới", count));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }


    /**
     * Method tinh tong so don hang voi trang thai dang doi thanh toan
     * @return number
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orderWaitPay/{user_id}")
    public ResponseEntity<ObjectResponse> countOrderWaitPay(@PathVariable("user_id") int id) {
        try {
            User userExisting = userService.getUserByID(id);
            if(userExisting == null) {
                return ResponseEntity.badRequest().body(null);
            }
            List<Order> orders = orderService.getStatusByUser(userExisting.getUserID(), EnumOrderStatus.Chờ_thanh_toán.name().replaceAll("_"," "));
            if(orders == null || orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success", "Không có đơn hàng đợi thanh toán", 0));
            }
            int count = orders.size();
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Số lượng đơn hàng đang đợi thanh toán", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    /**
     * Method update cac trang thai order
     * dua vao trang thasi de thuc hien mot so quy trinh nhu tao diem,...
     * @return message success or fail
     */
    @PreAuthorize("hasRole('USER') or hasRole('STAFF')")
    @PutMapping("/update_status/{order_id}")
    public ResponseEntity<ObjectResponse> updateStatusOrder(@PathVariable("order_id") int id, @RequestBody ConfirmOrderRequest confirmOrderRequest) {
        try {
            Order orderExisting = orderService.getOrderByOrderID(id);
            if (orderExisting == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Đơn hàng không tồn tại", null));
            }
            if(confirmOrderRequest.getStatus().equalsIgnoreCase(EnumOrderStatus.Chờ_giao_hàng.name().replaceAll("_", " "))) {
                List<User> deliverys = userService.getDeliveryWithLeastOrder();
                if(!deliverys.isEmpty()) {
                    User delivery = deliverys.get(0);
                    orderExisting.setDeliveryID(delivery);
                }
                orderExisting.setStatus(confirmOrderRequest.getStatus());
                orderExisting.setReason(null);
                pointsService.createPoints(orderExisting.getUser().getUserID(), orderExisting.getOrderID());
            } else if(confirmOrderRequest.getStatus().equalsIgnoreCase(EnumOrderStatus.Đã_giao.name().replaceAll("_"," "))) {
                orderExisting.setStatus(confirmOrderRequest.getStatus());
                orderExisting.setReason(null);
                pointsService.createPoints(orderExisting.getUser().getUserID(), orderExisting.getOrderID());
                List<OrderDetail> orderDetails = orderExisting.getOrderDetails();
                for(OrderDetail orderDetail : orderDetails) {
                    WarrantyCard newWarrantyCard = new WarrantyCard();
                    User existingUser = userService.getUserByID(orderExisting.getUser().getUserID());
                    if(existingUser == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Người dùng không tồn tại", null));
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
            } else if (confirmOrderRequest.getStatus().equalsIgnoreCase(EnumOrderStatus.Đã_hủy.name().replaceAll("_"," "))
                    || confirmOrderRequest.getStatus().equalsIgnoreCase(EnumOrderStatus.Không_Thành_Công.name().replaceAll("_"," "))) {
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
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Chỉnh sửa thành công", orderExisting));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }


    /**
     * Method get all order
     * @return list order
     */
    @PreAuthorize("hasRole('USER') or hasRole('STAFF') or hasRole('ADMIN') or hasRole('DELIVERY')")
    @GetMapping("/all_orders")
    public ResponseEntity<ObjectResponse> getAllOrders() {
        try {
            List<Order> list = orderService.getAllOrder();
            if(list == null || list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Danh sách đơn hàng rỗng", null));
            }
            list.sort(Comparator.comparing(Order::getOrderDate).reversed());
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Danh sách đơn hàng", list));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orders_by_diamond/{id}")
    public ResponseEntity<ObjectResponse> getOrdersByDiamondID(@PathVariable String id) {
        try {
            List<Order> orders = orderService.getOrderByDiamondID(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Đơn hàng", orders));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    /**
     * Method get orderDetail
     * @param id orderid
     * @return order
     */
    @PreAuthorize("hasRole('USER') or hasRole('STAFF') or hasRole('DELIVERY')")
    @GetMapping("/{order_id}")
    public ResponseEntity<ObjectResponse> getOrdersById(@PathVariable("order_id") int id) {
        try {
            Order orderExisting = orderService.getOrderByOrderID(id);
            if(orderExisting == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Đơn hàng không tồn tại với ID " + id, null));
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
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Lấy đơn hàng thành công", orderDetailRespone));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view_detail_admin/{order_id}")
    public ResponseEntity<ObjectResponse> viewDetailAdmin(@PathVariable("order_id") int id) {
        try {
            Order orderExisting = orderService.getOrderByOrderID(id);
            if(orderExisting == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Đơn hàng không tồn tại với ID " + id, null));
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
            String fullNameStaff = orderExisting.getStaffID().getFirstName() + " " +orderExisting.getStaffID().getLastName();
            String fulNameDelivery = orderExisting.getDeliveryID().getFirstName() + " " + orderExisting.getDeliveryID().getLastName();
            orderDetailRespone.setStaffName(fullNameStaff);
            orderDetailRespone.setDeliveryName(fulNameDelivery);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Lấy đơn hàng thành công", orderDetailRespone));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/order_details_by_order/{id}")
    public ResponseEntity<ObjectResponse> getOrderDetailsByOrderID(@PathVariable int id) {
        try {
            List<OrderDetail> orderDetails = orderService.getOrderDetailsByOrderID(id);
            if (orderDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Không có đơn hàng chi tiết với ID " + id, null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Lấy đơn hàng chi tiết thành công", orderDetails));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    /**
     * Method get order by  user
     * @param id userId
     * @return List order
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orders_by_user/{id}")
    public ResponseEntity<ObjectResponse> getOrdersByUserID(@PathVariable int id) {
        try {
            List<Order> orders = orderService.getOrderByUserID(id);
            if(orders == null || orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Không tim thấy đơn hàng với ID " +id, null));
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
            }
            newOrderRespone.setOrders(orderRespones);
            newOrders.add(newOrderRespone);
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Lấy đơn hàng thành công", newOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    /**
     * Method get order with status cho xac nhan
     * @return list new order
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/newest_order/{staff_id}")
    public ResponseEntity<ObjectResponse> getNewestOrder(@PathVariable("staff_id") int id) {
        try {
            User userExisting = userService.getUserByID(id);
                if(userExisting == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Nhân viên không tồn tại", null));
                }
            List<Order> newestOrders = orderService.getNewestOrderStaff(id, EnumOrderStatus.Chờ_xác_nhận.name().replaceAll("_"," "));
            if (newestOrders == null || newestOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Danh sách đơn hàng mới rỗng", null));
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
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Lấy Danh sách đơn hàng mới thành công", newOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    /**
     * Method get order by status
     * @return list order by status
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orders_by_status/{status}")
    public ResponseEntity<ObjectResponse> getOrdersByStatus(@PathVariable String status) {
        try {
            List<Order> newestOrders = orderService.getOrdersByStatus(status);
            if (newestOrders == null || newestOrders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Danh sách đơn hàng "+ status +" rỗng", null));
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

            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Danh sách đơn hàng "+ status +" rỗng", newOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    /**
     * Method tao order moi
     * @param orderRequest orderRequest
     * @return message success or fail
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/submit_order")
    public ResponseEntity<ObjectResponse> submitOrder(@RequestBody @Valid OrderRequest orderRequest) {
        try {
            User existingUser = userService.getUserByID(orderRequest.getUserID());
            if(existingUser == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Người dùng không tồn tại", null));
            }
            List<User> staffs = userService.getStaffWithLeastOrder();
            Order newOrder = new Order();
            if(!staffs.isEmpty()) {
                User staff = staffs.get(0);
                newOrder.setStaffID(staff);
            }
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


//            Map<Integer, Integer> countByStaffAdvised = getCountByStaffAdvised();
//
//
//            for(Integer staffID : countByStaffAdvised.keySet()) {
//
//            }


//            List<Order> list_order = orderService.getAllOrder();
//            List<User> lists = new ArrayList<>();
//            for(Order liOrders : list_order) {
//                if(liOrders.getStatus().toLowerCase().equals((EnumOrderStatus.Chờ_xác_nhận).name().replaceAll("_", " ").toLowerCase())) {
//                    User user = userService.getUserByID(liOrders.getStaffID().getUserID());
//                    lists.add(user);
//                }
//            }


            Cart existingCart = cartService.getCart(existingUser.getUserID());
            if(existingCart == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Giỏ hàng ko tồn tại", null));
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
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Kim cương đã hết hàng", null));
                    }
                    diamond.setStatus(false);
                    diamondService.saveDiamond(diamond);
                    orderDetail.setDiamond(diamond);
                }
                if (cartItem.getProductCustomize() != null) {
                    ProductCustomize productCustomize = cartItem.getProductCustomize();
                    Diamond diamondInProductCustomize = productCustomize.getDiamond();
                    if(!diamondInProductCustomize.isStatus()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Kim cương đã hết hàng", null));
                    }
                    diamondInProductCustomize.setStatus(false);
                    diamondService.saveDiamond(diamondInProductCustomize);
                    orderDetail.setProductCustomize(productCustomize);
                }
                newOrder.setQuantity(newOrder.getQuantity() + orderDetail.getQuantity());
                orderDetails.add(orderDetail);
            }
            if (orderDetails.isEmpty() || orderDetails == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Sản phẩm trống", null));
            }
            orderService.saveOrder(newOrder, orderDetails);
            cartService.clearCart(newOrder.getUser().getUserID());
            if(orderRequest.getUsedPoint() > 0.0) {
                pointsService.getUserPoints(orderRequest.getUserID(), newOrder.getOrderID(), orderRequest.getUsedPoint());
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Tạo đơn hàng thành công", newOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed", "Message: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/all_by_staff/{staff_id}")
    public ResponseEntity<ObjectResponse> getAllStaff(@PathVariable("staff_id") int staffId) {
        try {
            List<Order> listOrderWithStaff = orderService.getAllWithStaff(staffId);
            if(listOrderWithStaff == null || listOrderWithStaff.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed","Danh sách đơn hàng rỗng", null));
            }
            listOrderWithStaff.sort(Comparator.comparing(Order::getOrderDate).reversed());
            return ResponseEntity.status(HttpStatus.OK).body(new ObjectResponse("Success","Danh sách đơn hàng", listOrderWithStaff));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ObjectResponse("Failed","Message" + e.getMessage(), null));
        }
    }

//    @PreAuthorize("hasRole('USER')")
//    @DeleteMapping("/delete_order/{id}")
//    public ResponseEntity<?> deleteOrder(@PathVariable int id, @RequestBody ConfirmOrderRequest confirmOrderRequest) {
//        try {
//            orderService.markOrderAsDeleted(id,confirmOrderRequest.getStatus(), confirmOrderRequest.getReason());
//            return ResponseEntity.ok("Order deleted successfully with ID: " + id);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }


//    private Map<Integer, Integer> getCountByStaffAdvised() {
//        Map<Integer, Integer> countByStaffAdvised = new HashMap<>();
//        List<Order> lists = orderService.getAllOrder();
//        for(Order order : lists) {
//            if(order.getStaffID() != null) {
//                for(Integer i : countByStaffAdvised.keySet()) {
//                    if(order.getStaffID().getUserID() == i) {
//                        countByStaffAdvised.put(order.getStaffID().getUserID(), countByStaffAdvised.get(i) + 1);
//                    } else {
//                        countByStaffAdvised.put(order.getStaffID().getUserID(), 1);
//                    }
//                }
//            }
//        }
//        return countByStaffAdvised;
//    }

//    private boolean checkStaffProcessingOrder(User user) {
//        boolean check = false;
//        List<Order> list_order = orderService.getAllOrder();
//        for(Order order : list_order) {
//            if(order.getStatus().toLowerCase().equals((EnumOrderStatus.Chờ_xác_nhận).name().replaceAll("_", " ").toLowerCase())) {
//                User users = userService.getUserByID(order.getStaffID().getUserID());
//                if(users.equals(user)) {
//                    check = true;
//                }
//            }
//        }
//        return check;
//    }


//
//    private Map<Integer, Integer> count() {
//        Map<Integer, Integer> countByStaffAdvised = new HashMap<>();
//
//        List<Order> list_Orders = orderService.getAllOrder();
//        for(Order order : list_Orders) {
//            if(order.getStaffID() != null) {
//                User staff = userService.getUserByID(order.getStaffID().getUserID());
////                addMap(countByStaffAdvised, countStaffAdvised(staff), order.getStaffID().getUserID());
//
//            }
//        }
//
//
//    }
//
//    private void addMap(Map<Integer, Integer> countByStaffAdvised, int value, int staffID) {
//        for(Integer i : countByStaffAdvised.keySet()) {
//            if(i == staffID) {
//                int n = countByStaffAdvised.get(i) + value;
//                countByStaffAdvised.put(i, n);
//            } else {
//                countByStaffAdvised.put(i, countByStaffAdvised.get(i));
//            }
//        }
//    }
}
