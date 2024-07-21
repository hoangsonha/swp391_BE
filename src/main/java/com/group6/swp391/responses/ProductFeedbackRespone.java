package com.group6.swp391.responses;

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
