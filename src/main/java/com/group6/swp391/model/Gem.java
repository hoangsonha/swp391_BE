package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Gem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gem_id")
    private int gemId;

    private double price;

    @ManyToOne
    @JoinColumn(name = "carat_id")
    private Carat carat;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "clarify_id")
    private Clarify clarify;

    @ManyToOne
    @JoinColumn(name = "cut_id")
    private Cut cut;

    public Gem(Carat carat, Color color, Clarify clarify, Cut cut) {
        this.carat = carat;
        this.color = color;
        this.clarify = clarify;
        this.cut = cut;
    }
}
