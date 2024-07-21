package com.group6.swp391.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.group6.swp391.enums.EnumGenderName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    @JsonIgnore
    private User staffID;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    @JsonIgnore
    private User deliveryID;

    private boolean isDelivery;

    private int quantity;

    @Column(name = "full_name", nullable = false, columnDefinition = "nvarchar(50)")
    private String fullName;

    @Column(name = "address_shipping", nullable = false, columnDefinition = "nvarchar(300)")
    private String addressShipping;

    @Column(name = "phone_shipping", nullable = false, length = 12)
    private String phoneShipping;

    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties("order")
    private List<Payment> payments = new ArrayList<>();

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }

    private double price;

    @Column(name = "status", columnDefinition = "NVARCHAR(20)")
    private String status;

    @Column(name = "reason", columnDefinition = "NVARCHAR(300)")
    private String reason;

    private boolean deleteStatus;

    private String note;

    private String email;

    private double discount;

    @Column(name= "gender")
    private EnumGenderName gender;

    public Order(String addressShipping, String fullName, LocalDateTime orderDate,
                 List<Payment> payment, String phoneShipping, double price, int quantity,
                 String status, User user, String note) {
        this.addressShipping = addressShipping;
        this.fullName = fullName;
        this.orderDate = orderDate;
        this.payments = payment;
        this.phoneShipping = phoneShipping;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.user = user;
        this.note = note;
    }

    public Order(String addressShipping, String fullName, LocalDateTime orderDate,
                 List<Payment> payment, String phoneShipping, double price, int quantity,
                 String status, User user, User staffID, User deliveryID, String note) {
        this.addressShipping = addressShipping;
        this.fullName = fullName;
        this.orderDate = orderDate;
        this.payments = payment;
        this.phoneShipping = phoneShipping;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.user = user;
        this.staffID = staffID;
        this.deliveryID = deliveryID;
        this.note = note;
    }
}
