package com.group6.swp391.requests;

import com.group6.swp391.enums.EnumGenderName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInformation {

    @Email(message = "Email không hợp lệ")
    @NotNull(message = "Vui lòng điền email")
    @NotBlank(message = "Email không được để trống")
    @Size(max = 255, min = 10, message = "Email phải từ 10 tới 255 kí tự bao gồm cả @gmail.com")
    private String email;

    private String firstName;

    private String lastName;

    private String avata;

    private String address;

    private EnumGenderName gender;

    private Date yearOfBirth;

    private String phoneNumber;
}
