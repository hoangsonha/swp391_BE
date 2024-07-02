package com.group6.swp391.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group6.swp391.model.CollectionProduct;
import com.group6.swp391.model.Thumnail;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CollectionRespone {
    private String collectionId;
    private String collectionTitle;
    private String collectionName;
    private double price;
    private String gemStone;
    private String goldType;
    private String goldOld;
    private boolean status;
    @JsonIgnore
    private List<CollectionProductRespone> collectionProductRespones;
    private List<Thumnail> thumnails;

}
