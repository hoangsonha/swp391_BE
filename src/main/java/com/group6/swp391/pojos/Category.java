package com.group6.swp391.pojos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryID;

    @Column(name = "categpry_name", nullable = false, length = 50)
    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
