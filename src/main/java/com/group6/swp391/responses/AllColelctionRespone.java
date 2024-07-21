package com.group6.swp391.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group6.swp391.pojos.Thumnail;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AllColelctionRespone {
    private String collectionId;
    private String collectionTitle;
    private double price;
    @JsonIgnore
    private Thumnail thumnail;
    public String getThumbnailUrl() {
        return thumnail != null ? thumnail.getImageUrl() : null;
    }
}
