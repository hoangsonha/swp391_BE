package com.group6.swp391.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "size_id")
    @JsonIgnore
    private Size size;

    @Column(name = "quantity", nullable = false)
    private int quantiy;

    // Getter for sizeValue
    @JsonProperty("sizeValue")
    public int getSizeValue() {
        return size.getSizeValue();
    }
}
