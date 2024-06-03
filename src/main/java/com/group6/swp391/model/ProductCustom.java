package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ProductCustom {
    //thang nay sinh ra sau khi nhan addcart
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prodcut_custom_id")
    private  int prodcutCustomId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "diamond_id")
    private Diamond diamond;

    @Column(name = "total_name")
    private double totalPrice; // totalPrice = (diamondPirce + productPrice + wagePrice) * (1 + ratio))

    @Column(name = "ratio")
    private double ratio;

    @Column(name = "wage_price")
    private double wagePrice;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warranty_card_id", unique = true)
    private WarrantyCard warrantyCard;
}
