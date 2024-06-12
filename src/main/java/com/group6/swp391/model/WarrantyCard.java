package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warranty_card_id")
    private int warrantyCardID;

    @Column(name = "purchase_date")
    private Date purchaseDate;

    @PrePersist
    protected void onCreate() {
        this.purchaseDate = new Date();
    }

    @Column(name = "expiration_date")
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "product_customize_id", unique = true)
    private ProductCustomize productCustomize;

    @OneToOne
    @JoinColumn(name = "diamond_id", unique = true)
    private Diamond diamond;
}
