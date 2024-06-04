package com.group6.swp391.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id")
    private int sizeID;

    @Column(name = "size_value")
    private int sizeValue;

    @OneToMany(mappedBy = "size")
    @JsonIgnoreProperties("size")
    private List<ProductSize> productSizes;

}
