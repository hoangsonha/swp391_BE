package com.group6.swp391.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size {
    private int sizeID;
    private String sizeName;
    private int categoryID;
}
