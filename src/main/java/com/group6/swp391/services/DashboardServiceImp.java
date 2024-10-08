package com.group6.swp391.services;

import com.group6.swp391.pojos.Diamond;
import com.group6.swp391.pojos.Order;
import com.group6.swp391.pojos.Product;
import com.group6.swp391.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardServiceImp implements DashboardService {
    @Autowired OrderRepository orderRepository;
    @Autowired OrderDetailRepository orderDetailRepository;
    @Autowired UserRepository userRepository;
    @Autowired DiamondRepository diamondRepository;
    @Autowired ProductRepository productRepository;


    @Override
    public List<Order> totalRevenueStore() {
        return orderRepository.findByStatus("Đã giao");
    }

    @Override
    public Double getTotalRevenueInMonth(int month, int year) {
        Double revenue = orderRepository.findTotalRevenueInMonth(month, year);
        return (revenue != null) ? revenue : 0.0;
    }

    @Override
    public Double getTotalRevenueDiamond(int month, int year) {
        Double revenue = orderDetailRepository.findTotalDiamondRevenueInMonth(month, year);
        return (revenue != null) ? revenue : 0.0;
    }

    @Override
    public Double getTotalRevenueProductcustomizer(int month, int year) {
        Double revenue = orderDetailRepository.findTotalProductCustomizeRevenueInMonth(month, year);
        return (revenue != null) ? revenue : 0.0;
    }

    @Override
    public List<Order> getOrderByStatus(int month, String status) {
        return orderRepository.findByStatusMonth(month, status);
    }

    @Override
    public Integer newUser(int month, int year) {
        return userRepository.newuser(month, year);
    }

    @Override
    public Double totalRevenueDate(LocalDateTime startDate,LocalDateTime endDate) {
        return orderRepository.totalRevenueInDate(startDate, endDate);
    }

    @Override
    public List<Diamond> getNewDiamonds(int month, int year) {
        return diamondRepository.findByCreateAtMonth(month, year);
    }

    @Override
    public List<Product> getNewProducts(int month, int year) {
        return productRepository.findByCreateInMOnth(month, year);
    }

    @Override
    public List<Order> getOrderDate(String status, LocalDateTime startdate, LocalDateTime endtdate) {
        return orderRepository.getStatusIntdate(status, startdate, endtdate);
    }

}
