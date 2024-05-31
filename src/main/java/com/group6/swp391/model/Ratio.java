package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ratio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ratio_id")
    private int ratioId;

    @Column(name = "ratio_name", columnDefinition = "NVARCHAR(50)", nullable = false)
    private String ratioName;

    @Column(name = "ratio_current")
    private double ratioCurrent;
}
