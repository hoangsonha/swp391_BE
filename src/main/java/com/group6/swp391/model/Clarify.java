package com.group6.swp391.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Clarify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clarifyID;
    private String clarifyName;
}
