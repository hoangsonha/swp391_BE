package com.group6.swp391.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CollectionProductRequest {
    private int collectionProductId;
    private String productId;
}
