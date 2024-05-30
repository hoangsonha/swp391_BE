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

    @Column(name = "collection_name", nullable = false, columnDefinition = "varchar(300)")
    private String collectionName;

    @Column(name = "description", columnDefinition = "varchar(500)")
    private String description;
}
