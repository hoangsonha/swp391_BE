package com.group6.swp391.model;

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
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int  productID;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @JoinColumn(name = "price", nullable = false)
    private double price;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column(name = "image")
    private String image;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "diamond_id", referencedColumnName = "diamond_id")
    private Diamond diamond;

    @Column(name = "quantity_Stones_of_Diamond")
    private int quantityStonesOfDiamond;

    @Column(name = "wage", nullable = false)
    private double wage;

    @Column(name = "totalPrice", nullable = false)
    private double totalPrice;

    @Column(name = "rating")
    private double rating;

    @Column(name = "status")
    private boolean status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warranty_card_id", unique = true)
    private WarrantyCard warrantyCard;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_size", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "size_id"))
    private Set<Size> sizes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Collection_product", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "collection_id"))
    private Set<Collection> collections = new HashSet<>();

    public Product(String productName, Category category, double price, String brand, String image, Diamond diamond, int quantityStonesOfDiamond, double wage, double totalPrice, double rating, Set<Size> sizes) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.brand = brand;
        this.image = image;
        this.diamond = diamond;
        this.quantityStonesOfDiamond = quantityStonesOfDiamond;
        this.wage = wage;
        this.totalPrice = totalPrice;
        this.rating = rating;
        this.sizes = sizes;
    }
}
