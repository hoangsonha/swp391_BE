package com.group6.swp391.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Collection extends BaseEntity {

    @Id
    @Column(name = "collection_id", nullable = false, columnDefinition = "NVARCHAR(300)")
    private String collecitonId;

    @Column(name = "collection_title", nullable = false, columnDefinition = "NVARCHAR(300)")
    private String collectionTitle;

    @Column(name = "collection_name", nullable = false, columnDefinition = "NVARCHAR(300)")
    private String collectionName;

    private double price;

    @Column(name = "gem_stone", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String gemStone;

    @Column(name = "gole_type")
    private String goldType;

    @Column(name = "gold_old")
    private String goldOld;

    private boolean status;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("collection")
    private List<CollectionProduct> collectionProduct;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("collection")
    private List<Thumnail> thumnails;
}
