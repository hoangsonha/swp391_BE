package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCustom {
    //thang nay sinh ra sau khi nhan addcart
    @Id
    @Column(name = "prodcut_custom_id")
    private  String prodcutCustomId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "diamond_id")
    private Diamond diamond;

    @Column(name = "total_name")
    private double totalPrice; // totalPrice = (diamondPirce + productPrice + wagePrice) * (1 + ratio))

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warranty_card_id", unique = true)
    private WarrantyCard warrantyCard;

    public ProductCustom(Product product, double totalPrice, WarrantyCard warrantyCard) {
        this.product = product;
        this.totalPrice = totalPrice;
        this.warrantyCard = warrantyCard;
    }
}
