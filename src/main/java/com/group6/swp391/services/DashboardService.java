package com.group6.swp391.services;

import com.group6.swp391.pojos.Diamond;
import com.group6.swp391.pojos.Order;
import com.group6.swp391.pojos.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface DashboardService {
    List<Order> totalRevenueStore();
    Double getTotalRevenueInMonth(int month, int year);
    Double getTotalRevenueDiamond(int month, int year);
    Double getTotalRevenueProductcustomizer(int month, int year);
    List<Order> getOrderByStatus(int month, String status);
    Integer newUser(int month, int year);
    Double totalRevenueDate(LocalDateTime startdate,LocalDateTime endtdate);
    List<Diamond> getNewDiamonds(int month, int year);
    List<Product> getNewProducts(int month, int year);
    List<Order> getOrderDate(String status, LocalDateTime startdate, LocalDateTime endtdate);
}
