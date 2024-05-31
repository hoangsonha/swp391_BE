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
    private double paymentAmount;
    private double totalAmount;
    private double remainingAmount;

    public Payment(double paymentAmount, Order order, Date paymentDate, double remainingAmount, double totalAmount) {
        this.paymentAmount = paymentAmount;
        this.order = order;
        this.paymentDate = paymentDate;
        this.remainingAmount = remainingAmount;
        this.totalAmount = totalAmount;
    }
}
