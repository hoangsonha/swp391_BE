package com.group6.swp391.responses;

import com.group6.swp391.pojos.Thumnail;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CollectionDetailRespone {
    private String collectionId;
    private String collectionTitle;
    private String collectionName;
    private double price;
    private String gemStone;
    private String goldType;
    private String goldOld;
    private boolean status;
    private List<CollectionProductRespone> collectionProductRespones;
    private List<Float> sizeDiamond;
    private List<Thumnail> thumnails;

}
