package com.group6.swp391.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInformation {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String avata;
    private String address;
}
