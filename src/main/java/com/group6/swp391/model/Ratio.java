package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Ratio extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ratio_id")
    private int ratioId;

    @Column(name = "ratio_name", columnDefinition = "nvarchar(20)", nullable = false)
    private String ratioName;

    @Column(name = "current_ratio", nullable = false)
    private double currentRatio;
}
