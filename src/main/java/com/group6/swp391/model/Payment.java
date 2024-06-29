package com.group6.swp391.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity(name = "payment")
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;
    private Date paymentDate;
    private double paymentAmount; //50%
    private double totalAmount;
    private double remainingAmount;
    private String transactionId;
    private String methodPayment;
    private String status;

    public Payment(double paymentAmount, Order order, Date paymentDate, double remainingAmount, double totalAmount, String transactionId, String methodPayment, String status) {
        this.paymentAmount = paymentAmount;
        this.order = order;
        this.paymentDate = paymentDate;
        this.remainingAmount = remainingAmount;
        this.totalAmount = totalAmount;
        this.transactionId = transactionId;
        this.methodPayment = methodPayment;
        this.status = status;
    }
}
