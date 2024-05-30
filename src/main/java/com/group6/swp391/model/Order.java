package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int quantity;

    @Column(name = "full_name", nullable = false,columnDefinition = "varchar(300)")
    private String fullName;

    @Column(name = "address_shipping", nullable = false, columnDefinition = "varchar(300)")
    private String addressShipping;

    @Column(name = "phone_shipping", nullable = false, length = 12)
    private String phoneShipping;

    private double payment;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }
    private double price;

    private String status;

    public Order(User user, int quantity, String addressShipping, String phoneShipping, double payment, LocalDateTime orderDate, double price, String status) {
        this.user = user;
        this.quantity = quantity;
        this.addressShipping = addressShipping;
        this.phoneShipping = phoneShipping;
        this.payment = payment;
        this.orderDate = orderDate;
        this.price = price;
        this.status = status;
    }
}
