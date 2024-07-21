package com.group6.swp391.services;

import com.group6.swp391.pojos.OrderDetail;
import com.group6.swp391.repositories.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImp implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> getOrderDetailsByOrderID(int orderID) {
        return orderDetailRepository.findByOrderOrderID(orderID);
    }
}
