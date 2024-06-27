package com.group6.swp391.service;

import com.group6.swp391.repository.OrderDetailRepository;
import com.group6.swp391.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImp implements DashboardService {
    @Autowired OrderRepository orderRepository;
    @Autowired OrderDetailRepository orderDetailRepository;


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
}
