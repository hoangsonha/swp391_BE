package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id")
    private int colorID;

    @Column(name = "color_name", nullable = false, length = 150)
    private String colorName;

    public Color(String colorName) {
        this.colorName = colorName;
    }
}
