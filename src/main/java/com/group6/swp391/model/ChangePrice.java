package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    @Column(name = "wage", nullable = false)
    private double wage;

    @Column(name = "sheath_diamond", nullable = false)
    private double sheathDiamond;

    @Column(name = "priceDiamond")
    private double priceDiamond;

    @ManyToOne
    @JoinColumn(name = "ratio_id")
    private Ratio ratio;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime timestamp;


    public ChangePrice(double wage, double sheathDiamond, double totalPrice, double priceDiamond, Ratio ratio, Product product, LocalDateTime timestamp) {
        this.wage = wage;
        this.sheathDiamond = sheathDiamond;
        this.totalPrice = (wage + sheathDiamond + priceDiamond) * ratio.getCurrentRatio();
        this.priceDiamond = priceDiamond;
        this.ratio = ratio;
        this.product = product;
        this.timestamp = timestamp;
    }
}
