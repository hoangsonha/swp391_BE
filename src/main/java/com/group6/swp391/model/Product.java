package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {
    @Id
    @Column(name = "product_id", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String productID;

    @Column(name = "product_name", nullable = false, columnDefinition = "NVARCHAR(300)")
    private String productName;

    @Column(name = "bath_stone",nullable = false,columnDefinition = "NVARCHAR(100)")
    private String bathStone;

    @Column(name = "brand",columnDefinition = "NVARCHAR(50)")
    private String brand;

    @Column(name = "gole_type",columnDefinition = "NVARCHAR(50)")
    private String goleType;

    @Column(name = "gold_weight")
    private float goldWeight;

    @Column(name = "cut_diamond")
    private String cutDiamond;

    @Column(name = "dimensions_diamond")
    private float dimensionsDiamond;

    @Column(name = "message",columnDefinition = "NVARCHAR(50)")
    private String message;

    @Column(name = "old_gold",columnDefinition = "NVARCHAR(50)")
    private String oldGold;

    @Column(name = "product_type", columnDefinition = "NVARCHAR(50)")
    private String productType;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "quantity_stones_of_diamond")
    private int quantityStonesOfDiamond;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "rating")
    private double rating;

    @Column(name = "status")
    private boolean status;

    @Column(name = "original_price")
    private double originalPrice;

    @Column(name = "ratio")
    private double ratio;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Thumnail> productImages;

    @OneToMany(mappedBy = "product")
    private List<ProductSize> productSizes;


}
