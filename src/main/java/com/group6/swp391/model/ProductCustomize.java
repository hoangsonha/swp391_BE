package com.group6.swp391.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.group6.swp391.model.Cart;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCustomize {
    //thang nay sinh ra sau khi nhan addcart
    @Id
    @Column(name = "prodcut_custom_id")
    private String prodcutCustomId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "diamond_id")
    private Diamond diamond;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "size")
    private int size;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warranty_card_id", unique = true)
    private WarrantyCard warrantyCard;
}

