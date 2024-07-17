package com.group6.swp391.request;

import lombok.*;


@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerInquiryRequest {
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String note;
    private String collectionid;

}
