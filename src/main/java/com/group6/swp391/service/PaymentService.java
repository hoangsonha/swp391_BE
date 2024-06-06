package com.group6.swp391.service;

import com.group6.swp391.model.Payment;

import java.util.List;

public interface PaymentService {
    void save(Payment payment);

    Payment createPayment(Payment payment);

    void createPayments(List<Payment> payments);
}
