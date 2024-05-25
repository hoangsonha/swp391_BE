package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cut_id")
    private int cutId;

    @Column(name = "cut_name", nullable = false, length = 100)
    private String cutName;

    public Cut(String cutName) {
        this.cutName = cutName;
    }
}
