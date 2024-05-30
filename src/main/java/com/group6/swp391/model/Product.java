package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {
    @Id
    @Column(name = "product_id")
    private String productID;

    @Column(name = "product_name", nullable = false, columnDefinition = "varchar(300)")
    private String productName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    @Column(name = "brand", nullable = false, columnDefinition = "varchar(100)")
    private String brand;

    @Column(name = "image")
    private String image;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "diamond_id", unique = true)
    private Diamond diamond;

    @Column(name = "quantity_Stones_of_Diamond")
    private int quantityStonesOfDiamond;

    @Transient
    @Column(name = "totalPrice", nullable = false)
    private double totalPrice;

    @Column(name = "rating")
    private double rating;

    @Column(name = "message", columnDefinition = "nvarchar(300)")
    private String message;

    @Column(name = "status")
    private boolean status;

    @Column(name = "bath_stone", columnDefinition = "varchar(100)")
    private String bathStone;

    @Column(name = "host_size", nullable = false)
    private String hostSize;

    @Column(name = "product_type", nullable = false, columnDefinition = "varchar(70)")
    private String productType;

    @Column(name = "goldType", nullable = false, columnDefinition = "varchar(70)")
    private String goldType;

    @Column(name = "old_gold", nullable = false)
    private String oldGold;

    @Column(name = "gold_weight", nullable = false)
    private float goldWeight;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warranty_card_id", unique = true)
    private WarrantyCard warrantyCard;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChangePrice> changePrices;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages;

    @PostLoad
    @PostPersist
    @PostUpdate
    private void calculateTotalPrice() {

        if (changePrices != null && !changePrices.isEmpty()) {
            ChangePrice latestChangePrice = Collections.max(changePrices, Comparator.comparing(ChangePrice::getTimestamp));
            totalPrice = latestChangePrice.getTotalPrice();
        }
    }

    public Product(String productName, Category category, String brand, String image, Diamond diamond,
                   int quantityStonesOfDiamond, double totalPrice, double rating, String message,
                   boolean status, String bathStone, String hostSize, String productType, String goldType, String oldGold,
                   float goldWeight, WarrantyCard warrantyCard, List<ChangePrice> changePrices) {
        this.productName = productName;
        this.category = category;
        this.brand = brand;
        this.image = image;
        this.diamond = diamond;
        this.quantityStonesOfDiamond = quantityStonesOfDiamond;
        this.totalPrice = totalPrice;
        this.rating = rating;
        this.message = message;
        this.status = status;
        this.bathStone = bathStone;
        this.hostSize = hostSize;
        this.productType = productType;
        this.goldType = goldType;
        this.oldGold = oldGold;
        this.goldWeight = goldWeight;
        this.warrantyCard = warrantyCard;
        this.changePrices = changePrices;
    }
}
