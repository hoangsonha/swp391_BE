package com.group6.swp391.service;

import com.group6.swp391.model.Diamond;
import com.group6.swp391.model.Order;
import com.group6.swp391.model.Product;

import java.time.LocalDate;
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
}
