package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String bath_Stone;

    @Column(name = "brand", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String brand;

    private String goleType;

    private float goldWeight;

    private String hostSize;

    private String message;

    private String oldGold;

    private String productType;

    private int quantityStonesOfDiamond;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @JoinColumn(name = "price", nullable = false)
    private double price;

    @Column(name = "quantity")
    private int quantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "diamond_id", referencedColumnName = "diamond_id")
    private Diamond diamond;


    @Column(name = "totalPrice", nullable = false)
    private double totalPrice;

    @Column(name = "rating")
    private double rating;

    @Column(name = "status")
    private boolean status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warranty_card_id", unique = true)
    private WarrantyCard warrantyCard;

    @ManyToOne
    @JoinColumn(name = "change_price_id")
    private ChangePrice changePrice;


    @Column(name = "ratio")
    private double ratio;

    public Product(String productName, String bath_Stone, String brand, String goleType,
                   float goldWeight, String hostSize, String message, String oldGold,
                   String productType, int quantityStonesOfDiamond, Category category,
                   double price, int quantity, List<ProductImage> productImages, Diamond diamond, double totalPrice,
                   double rating, boolean status, WarrantyCard warrantyCard, ChangePrice changePrice, double ratio) {
        this.productName = productName;
        this.bath_Stone = bath_Stone;
        this.brand = brand;
        this.goleType = goleType;
        this.goldWeight = goldWeight;
        this.hostSize = hostSize;
        this.message = message;
        this.oldGold = oldGold;
        this.productType = productType;
        this.quantityStonesOfDiamond = quantityStonesOfDiamond;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.productImages = productImages;
        this.diamond = diamond;
        this.totalPrice = (changePrice.getTotalPrice() + diamond.getTotalPrice() * ratio);
        this.rating = rating;
        this.status = status;
        this.warrantyCard = warrantyCard;
        this.changePrice = changePrice;
    }
}
