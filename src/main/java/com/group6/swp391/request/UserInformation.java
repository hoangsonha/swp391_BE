package com.group6.swp391.request;

import com.group6.swp391.enums.EnumGenderName;
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
    private EnumGenderName gender;
    private int yearOfBirth;
    private String phoneNumber;
}
