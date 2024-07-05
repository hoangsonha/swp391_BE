package com.group6.swp391.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diamond {
    @Id
    @Column(name = "diamond_id", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String diamondID;

    @Column(name = "diamond_name", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String diamondName;

    @Column(name = "carat", nullable = false)
    private float carat;

    @Column(name = "certificate", nullable = false)
    private String certificate;

    @Column(name = "clarify", nullable = false, length = 20)
    private String clarify;

    @Column(name = "color", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String color;

    @Column(name = "color_level", nullable = false, length = 5)
    private char colorLevel;

    @Column(name = "cut", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String cut;

    @Column(name = "shape", nullable = false, columnDefinition = "NVARCHAR(30)")
    private String shape;

    @Column(name = "dimensions")
    private float dimensions;

    @Column(name = "flourescence", nullable = false, columnDefinition = "NVARCHAR(20)")
    private String flourescence;

    @Column(name = "image")
    private String image;

    @Column(name = "input_date", nullable = false)
    private Date inputDate;

    @Column(name = "originPrice", nullable = false)
    private double originPrice;

    @Column(name = "status")
    private boolean status;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "ratio")
    private double ratio;

    @OneToMany(mappedBy = "diamond")
    @JsonIgnoreProperties("diamond")
    private List<Feedback> feedbacks;

}
