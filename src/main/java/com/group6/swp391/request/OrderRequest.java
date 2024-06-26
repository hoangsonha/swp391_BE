package com.group6.swp391.request;

import com.group6.swp391.enums.EnumGenderName;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String addressShipping;
    String fullName;
    String phoneShipping;
    double price;
    int userID;
    double usedPoint;
    String note;
    String email;
    double discount;
    EnumGenderName gender;
}
