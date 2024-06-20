package com.group6.swp391.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ResultRequest {
    private String status;
    private String message;
}
