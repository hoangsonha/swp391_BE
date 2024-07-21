package com.group6.swp391.requests;

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
