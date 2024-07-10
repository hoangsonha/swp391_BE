package com.group6.swp391.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class ProductFeedbackRespone {
    private String productID;
    private String productName;
}
