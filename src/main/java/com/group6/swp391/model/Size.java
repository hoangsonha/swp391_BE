package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id")
    private int sizeID;

    @Column(name = "size_name", nullable = false, length = 30)
    private String sizeName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Size(String sizeName, Category category) {
        this.sizeName = sizeName;
        this.category = category;
    }
}
