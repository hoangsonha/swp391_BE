package com.group6.swp391.services;

import com.group6.swp391.pojos.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> getOrderDetailsByOrderID(int orderID);
}
