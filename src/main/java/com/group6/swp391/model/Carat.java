package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Carat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carat_id")
    private int caratId;

    @Column(name = "carat_name", nullable = false, length = 150)
    private float caratName;

    public Carat(float caratName) {
        this.caratName = caratName;
    }
}
