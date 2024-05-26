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
    @Column(name = "diamond_id", nullable = false, length = 30)
    private char diamondID;

    @Column(name = "diamond_name", nullable = false, length = 200)
    private String diamondName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "warranty_card_id", referencedColumnName = "warranty_card_id")
    private WarrantyCard warrantyCard;

    @Column(name = "brand")
    private String brand;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gem_id", referencedColumnName = "gem_id")
    private Gem gem;

    @Column(name = "image")
    private String image;

    @Column(name = "certificate_number")
    private int certificateNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "flourescence_id", referencedColumnName = "flourescence_id")
    private Flourescence flourescence;

    @OneToMany(mappedBy = "diamond", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChangePrice> priceChanges;

    @Column(name = "input_date")
    private Date inputDate;

    public Diamond(String diamondName, WarrantyCard warrantyCard, String brand, Gem gem, String image, int certificateNumber, Flourescence flourescence, List<ChangePrice> priceChanges, Date inputDate) {
        this.diamondName = diamondName;
        this.warrantyCard = warrantyCard;
        this.brand = brand;
        this.gem = gem;
        this.image = image;
        this.certificateNumber = certificateNumber;
        this.flourescence = flourescence;
        this.priceChanges = priceChanges;
        this.inputDate = inputDate;
    }
}
