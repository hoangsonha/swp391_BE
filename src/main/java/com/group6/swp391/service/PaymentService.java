package com.group6.swp391.service;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.Payment;

import java.util.List;

public interface PaymentService {
    public void save(Payment payment);

    public List<Payment> findAll();

    public Payment findByOrder(Order order);

}
