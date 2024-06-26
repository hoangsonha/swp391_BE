package com.group6.swp391.service;

import com.group6.swp391.model.*;
import com.group6.swp391.repository.DiamondRepository;
import com.group6.swp391.repository.OrderDetailRepository;
import com.group6.swp391.repository.OrderRepository;
import com.group6.swp391.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private DiamondServiceImp diamondServiceImp;
    @Autowired PointsService pointsService;
    @Autowired
    private DiamondRepository diamondRepository;

    @Override
    public Order getOrderByOrderID(int orderID) {
        return orderRepository.getOrderByOrderID(orderID);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public void markOrderAsDeleted(int orderID,String status,String reason) {
        try {
            Order order = getOrderByOrderID(orderID);
            if (order != null) {
                order.setDeleteStatus(false);
                order.setStatus(status);
                order.setReason(reason);
                orderRepository.save(order);
            } else {
                throw new RuntimeException("Order not found with id: " + orderID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> getOrderByDiamondID(String diamondID) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByDiamondDiamondID(diamondID);
        List<Order> orders = new ArrayList<>();
        for (OrderDetail detail : orderDetails) {
            if (!orders.contains(detail.getOrder())) {
                orders.add(detail.getOrder());
            }
        }
        return orders;
    }

    @Override
    public List<Order> getOrderByUserID(int userID) {
        List<Order> orders = orderRepository.findByUserUserID(userID);
        return orders;
    }

    private void decrementSizeQuantity(ProductCustomize productCustomize, int quantity) {
        try {
            Product product = productCustomize.getProduct();
            int sizeValue = productCustomize.getSize();

            Size size = sizeRepository.findByProductAndSizeValue(product, sizeValue);
            if (size == null) {
                throw new RuntimeException("Size not found for product: "
                        + product.getProductID() + ", size: " + sizeValue);
            }
            if (size.getQuantity() < quantity) {
                throw  new RuntimeException("Not enough quantity for product: "
                        + product.getProductID() + ", size: " + sizeValue);
            }

            size.setQuantity(size.getQuantity() - quantity);
            sizeRepository.save(size);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Order saveOrder(Order order, List<OrderDetail> orderDetails) {
        try {
            Order savedOrder = orderRepository.save(order);
            for (OrderDetail detail : orderDetails) {
                detail.setOrder(savedOrder);
                if (detail.getProductCustomize() != null) {
                    decrementSizeQuantity(detail.getProductCustomize(), detail.getQuantity());
                }
            }
            orderDetailRepository.saveAll(orderDetails);
            savedOrder.setOrderDetails(orderDetails);
            return savedOrder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Order> getNewestOrder(String status) {
        return orderRepository.findTopByOrderByOrderDateDesc(status);
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public List<Order> getStatusByUser(int userId, String status) {
        return orderRepository.findByUserAndStatus(userId, status);
    }

    @Override
    public Order updateStatus(int orderID, String status, String reason) {
        try {
            Order order = getOrderByOrderID(orderID);
            if (order == null) {
                throw new RuntimeException("Order not found with id: " + orderID);
            }
            if (status.equalsIgnoreCase("Chờ giao hàng")) {
                pointsService.createPoints(order.getUser().getUserID(), orderID);
            }
            order.setStatus(status);
            order.setReason(reason);
            return orderRepository.save(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Order updateOrderStatus(int orderID, String status) {
        return updateOrderStatus(orderID, status, null);
    }

    @Override
    public Order updateOrderStatus(int orderID, String status, String reason) {
        try {
            Order order = orderRepository.findById(orderID).orElse(null);
            if (order == null) {
                throw new RuntimeException("Order does not exist");
            }

            if (status.equals("Đã hủy")) {
                for (OrderDetail detail : order.getOrderDetails()) {
                    Diamond diamond = detail.getDiamond();
                    if (diamond != null) {
                        diamond.setStatus(true);
                        diamondRepository.save(diamond);
                    }
                    if (detail.getProductCustomize() != null) {
                        incrementSizeQuantity(detail.getProductCustomize(), detail.getQuantity());
                    }
                }
            }
            if (reason != null) {
                order.setReason(reason);
            }
            order.setStatus(status);
            return orderRepository.save(order);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void incrementSizeQuantity(ProductCustomize productCustomize, int quantity) {
        try {
            Product product = productCustomize.getProduct();
            int sizeValue = productCustomize.getSize();

            Size size = sizeRepository.findByProductAndSizeValue(product, sizeValue);
            if (size == null){
                throw new RuntimeException("Size not found for product: "
                + product.getProductID() + ", size: " + sizeValue);
            }
            size.setQuantity(size.getQuantity() + quantity);
            sizeRepository.save(size);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


    //Phần code phục vụ dashboard
    @Override
    public double getByMonth(int month, int year) {
        return 0;
    }
}
