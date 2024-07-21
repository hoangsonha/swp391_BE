package com.group6.swp391.repositories;

import com.group6.swp391.pojos.Order;
import com.group6.swp391.pojos.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    public Payment findPaymentByOrder(Order order);

}
