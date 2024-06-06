package com.group6.swp391.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchAdvanceRequest {
    private String carat;
    private String size;
    private char color;
    private String clarify;
    private String shape;
    private String price;
    private String optionPrice;
}
