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


    @Column(name = "total_price", nullable = false)
    private double totalPrice;



    public ChangePrice(double wage, double sheathDiamond, double totalPrice) {
        this.wage = wage;
        this.sheathDiamond = sheathDiamond;
        this.totalPrice = wage + sheathDiamond;
    }
}
