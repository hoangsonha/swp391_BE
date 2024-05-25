package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flourescence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flourescence_id")
    private int flourescenceID;

    @Column(name = "flourescence_name")
    private String flourescenceName;

    public Flourescence(String flourescenceName) {
        this.flourescenceName = flourescenceName;
    }
}
