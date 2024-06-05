package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "order")
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

    @Column(name = "full_name", nullable = false,columnDefinition = "nvarchar(300)")
    private String fullName;

    @Column(name = "address_shipping", nullable = false, columnDefinition = "nvarchar(300)")
    private String addressShipping;

    @Column(name = "phone_shipping", nullable = false, length = 12)
    private String phoneShipping;

    @OneToMany(mappedBy = "order")
    private List<Payment> payments = new ArrayList<>();

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }
    private double price;

    private String status;

    public Order(String addressShipping, String fullName, LocalDateTime orderDate, List<Payment> payment, String phoneShipping, double price, int quantity, String status, User user) {
        this.addressShipping = addressShipping;
        this.fullName = fullName;
        this.orderDate = orderDate;
        this.payments = payment;
        this.phoneShipping = phoneShipping;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.user = user;
    }
}
