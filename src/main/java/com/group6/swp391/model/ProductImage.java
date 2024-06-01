package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id",nullable = false)
    private int imageId;

    @Column(name = "image_thumbnail")
    private String imageThumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductImage(String imageThumbnail, Product product) {
        this.imageThumbnail = imageThumbnail;
        this.product = product;
    }
}
