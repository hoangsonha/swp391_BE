package com.group6.swp391.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_product_Id")
    private int  collectionProductId;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
