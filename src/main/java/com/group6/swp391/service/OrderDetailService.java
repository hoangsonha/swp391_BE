package com.group6.swp391.service;

import com.group6.swp391.model.OrderDetail;

import java.time.LocalDate;
import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> getOrderDetailsByOrderID(int orderID);
}
