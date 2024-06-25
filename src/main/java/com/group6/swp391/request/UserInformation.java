package com.group6.swp391.request;

import com.group6.swp391.enums.EnumGenderName;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInformation {
    private String email;
    private String firstName;
    private String lastName;
    private String avata;
    private String address;
    private EnumGenderName gender;
    private Date yearOfBirth;
    private String phoneNumber;
}
