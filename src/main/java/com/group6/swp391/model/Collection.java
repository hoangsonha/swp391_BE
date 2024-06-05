package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Collection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private int collecitonId;

    @Column(name = "collection_name", nullable = false, length = 50)
    private String collectionName;

    @Column(name = "description", length = 1024)
    private String description;
}
