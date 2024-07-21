package com.group6.swp391.responses;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class CustomerInquiryDetailRespone {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String collectionId;
    private String collectionName;
    private String collectionImage;
    private List<CollectionProductRespone> collectionProductResponeList;
}
