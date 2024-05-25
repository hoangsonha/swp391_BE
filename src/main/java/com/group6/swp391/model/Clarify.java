package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Clarify")
public class Clarify {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "clarify_id")
    private int clarifyID;

    @Column(name = "clarify_name", nullable = false, length = 150)
    private String clarifyName;

    public Clarify(String clarifyName) {
        this.clarifyName = clarifyName;
    }
}
