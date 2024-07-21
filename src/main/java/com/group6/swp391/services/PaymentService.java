package com.group6.swp391.services;

import com.group6.swp391.pojos.Order;
import com.group6.swp391.pojos.Payment;

import java.util.List;

public interface PaymentService {
    public void save(Payment payment);

    public List<Payment> findAll();

    public Payment findByOrder(Order order);

}
