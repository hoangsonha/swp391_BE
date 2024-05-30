//package com.group6.swp391.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ProductImage {
//
//    @Id
//    @Column(name = "image_id", nullable = false)
//    private String imageId;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;
//
//    @Column(name = "image_url", nullable = false, columnDefinition = "varchar(255)")
//    private String imageLink;
//}
