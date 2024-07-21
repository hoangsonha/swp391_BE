package com.group6.swp391.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObjectResponse {
    private String code;
    private String message;
    private Object data;
}
