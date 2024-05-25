package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "change_price_id")
    private int changePriceID;

    @Column(name = "origin_price", nullable = false)
    private double originPrice;

    @Column(name = "change_price")
    private double changePrice; // changePrice = ratio * (wage + originPrice)

    @Column(name = "ratio", nullable = false)
    private double ratio;

    @ManyToOne
    @JoinColumn(name = "diamond_id")
    private Diamond diamond;

    @Column(name = "wage", nullable = false)
    private double wage;

    public ChangePrice(double originPrice, double changePrice, double ratio, double wage) {
        this.originPrice = originPrice;
        this.changePrice = changePrice;
        this.ratio = ratio;
        this.wage = wage;
    }
}
