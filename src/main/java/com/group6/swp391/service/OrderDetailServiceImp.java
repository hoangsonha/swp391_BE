package com.group6.swp391.service;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.OrderDetail;
import com.group6.swp391.repository.OrderDetailRepository;
import com.group6.swp391.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderDetailServiceImp implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> getOrderDetailsByOrderID(int orderID) {
        return orderDetailRepository.findByOrderOrderID(orderID);
    }

    @Override
    public Double getTotalDiamondRevenueInMonth(LocalDate startDate, LocalDate endDate) {
        return orderDetailRepository.findTotalDiamondRevenueInMonth(startDate, endDate);
    }

    @Override
    public Double getTotalProductcustomizeRevenueInMonth(LocalDate startDate, LocalDate endDate) {
        return orderDetailRepository.findTotalProductCustomizeRevenueInMonth(startDate, endDate);
    }
}
