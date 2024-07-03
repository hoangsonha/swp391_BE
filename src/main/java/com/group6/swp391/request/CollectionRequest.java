package com.group6.swp391.request;

import com.group6.swp391.model.Thumnail;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionRequest {
    private String collectionTitle;
    private String collectionName;
    private String gemStone;
    private String goldType;
    private String goldOld;
    private String status;
    private List<Thumnail> thumnails;
    private List<CollectionProductRequest> collectionProductRequests;
}
