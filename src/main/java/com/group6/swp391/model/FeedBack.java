package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedBack extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedBack_id")
    private int feedBackID;

    @Column(name = "comment")
    private String comment;

    private double rating;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    public FeedBack(String comment, double rating, Order order) {
        this.comment = comment;
        this.rating = rating;
        this.order = order;
    }
}
