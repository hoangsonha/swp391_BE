package com.group6.swp391.pojos;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCustomize {
    //thang nay sinh ra sau khi nhan addcart
    @Id
    @Column(name = "product_customize_id", columnDefinition = "varchar(50)")
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
}

