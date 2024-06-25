package com.group6.swp391.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class PasswordRequest {
    private String oldPassWord;
    private String newPassWord;
}
