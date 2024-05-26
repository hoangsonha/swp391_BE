package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Points {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "points_id")
    private int pointsID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int point;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
