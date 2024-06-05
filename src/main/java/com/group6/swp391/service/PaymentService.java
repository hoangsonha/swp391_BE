package com.group6.swp391.service;

import com.group6.swp391.model.Payment;

public interface PaymentService {
    public void save(Payment payment);

    public Payment createPayment(Payment payment);
}
