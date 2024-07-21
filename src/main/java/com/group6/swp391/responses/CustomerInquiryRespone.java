package com.group6.swp391.responses;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class CustomerInquiryRespone {
    private int id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String note;
    private String collectionId;
    private String collectionName;
    private String conllectionImage;
}
