package com.group6.swp391.request;

import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmOrderRequest {
    private String status;
    private String reason;
}
