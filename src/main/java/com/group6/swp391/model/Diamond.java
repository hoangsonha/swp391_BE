package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diamond {
    @Id
    @Column(name = "diamond_id", nullable = false, columnDefinition = "varchar(255)")
    private String diamondID;

    @Column(name = "diamond_name", nullable = false, columnDefinition = "varchar(255)")
    private String diamondName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warranty_card_id", referencedColumnName = "warranty_card_id", unique = true)
    private WarrantyCard warrantyCard;

    @Column(name = "color_level", nullable = false)
    private char colorLevel;

    @Column(name = "color", nullable = false, columnDefinition = "varchar(30)")
    private String color;
    @Column(name = "carat", nullable = false)
    private float carat;

    @Column(name = "clarify", nullable = false, columnDefinition = "varchar(60)")
    private String clarify;

    @Column(name = "cut", nullable = false, columnDefinition = "varchar(60)")
    private String cut;

    @Column(name = "image")
    private String image;

    @Column(name = "size")
    private float size;

    @Column(name = "certificate", columnDefinition = "varchar(100)")
    private String certificate;

    @Column(name = "flourescence", nullable = false, columnDefinition = "varchar(60)")
    private String flourescence;

    @Column(name = "dimensions", nullable = false)
    private float dimensions;

    @Column(name = "origin_price", nullable = false)
    private double originPrice;

    @Column(name = "total_price") // total = origin*ratio;
    private double totalPrice;

    @Column(name = "input_date")
    private Date inputDate;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "ratio_id")
    private Ratio ratio;

    public Diamond(String diamondName, WarrantyCard warrantyCard, char colorLevel,
                   String color, float carat, String clarify, String cut, String image, float size,
                   String certificate, String flourescence, float dimensions, double originPrice,
                   double totalPrice, Date inputDate, boolean status, Ratio ratio) {
        this.diamondName = diamondName;
        this.warrantyCard = warrantyCard;
        this.colorLevel = colorLevel;
        this.color = color;
        this.carat = carat;
        this.clarify = clarify;
        this.cut = cut;
        this.image = image;
        this.size = size;
        this.certificate = certificate;
        this.flourescence = flourescence;
        this.dimensions = dimensions;
        this.originPrice = originPrice;
        this.totalPrice = totalPrice * ratio.getCurrentRatio();
        this.inputDate = inputDate;
        this.status = status;
    }
}
