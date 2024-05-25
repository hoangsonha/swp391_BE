package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "expiration_date")
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private Product product;

    public WarrantyCard(Date purchaseDate, Date expirationDate, User user) {
        this.purchaseDate = purchaseDate;
        this.expirationDate = expirationDate;
        this.user = user;
    }
}
