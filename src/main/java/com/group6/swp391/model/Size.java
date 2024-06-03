package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id")
    private int sizeID;

    @Column(name = "size_value")
    private int sizeValue;

    @OneToMany(mappedBy = "size")
    private List<ProductSize> productSizes;

}
