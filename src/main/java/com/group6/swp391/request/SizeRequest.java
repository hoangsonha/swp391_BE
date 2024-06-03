package com.group6.swp391.request;

import lombok.*;

import javax.xml.parsers.SAXParser;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SizeRequest {
    private int sizeId;
    private int sizeValue;
    private int quantity;
}
