package com.group6.swp391.request;

import com.group6.swp391.model.Diamond;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomeRequest {
    private ProductRequest productRequest;
    private Diamond diamond;
    private double totalPrice;
}
