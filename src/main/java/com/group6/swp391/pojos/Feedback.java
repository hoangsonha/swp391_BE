package com.group6.swp391.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private int feedbackID;

    @Column(name = "comment", length = 1024)
    private String comment;

    private double rating;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Transient
    private int userId;

    @Transient
    private String fullName;

    public int getUserId() {
        return  user != null ? user.getUserID() : null;
    }

    public String getFullName() {
        return user != null ? ( user.getFirstName() + " " + user.getLastName()) : null;
    }

    public Feedback(String comment, double rating, Product product) {
        this.comment = comment;
        this.rating = rating;
        this.product = product;
    }
}
