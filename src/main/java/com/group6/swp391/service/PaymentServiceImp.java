package com.group6.swp391.service;

import com.group6.swp391.model.Order;
import com.group6.swp391.model.Payment;
import com.group6.swp391.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImp implements PaymentService {

    @Autowired private PaymentRepository paymentRepository;


    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment findByOrder(Order order) {
        return paymentRepository.findPaymentByOrder(order);
    }

}
