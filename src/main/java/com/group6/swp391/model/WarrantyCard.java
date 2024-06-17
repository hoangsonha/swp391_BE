package com.group6.swp391.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarrantyCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warranty_card_id")
    private int warrantyCardID;

    @Column(name = "purchase_date")
    private Date purchaseDate;

    @PrePersist
    protected void onCreate() {
        this.purchaseDate = new Date();
    }

    @Column(name = "expiration_date")
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public int getUserId() {
        return  user != null ? user.getUserID() : null;
    }

    public String getFullName() {
        return user != null ? ( user.getFirstName() + " " + user.getLastName()) : null;
    }

    @OneToOne
    @JoinColumn(name = "product_customize_id", unique = true)
    @JsonIgnoreProperties("warrantyCard")
    private ProductCustomize productCustomize;

    @OneToOne
    @JoinColumn(name = "diamond_id", unique = true)
    private Diamond diamond;
}
