package com.group6.swp391.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ObjectResponse {
    private String code;
    private String message;
    private Object data;
}
