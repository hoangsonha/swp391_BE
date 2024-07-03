package com.group6.swp391.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group6.swp391.enums.EnumGenderName;
import com.group6.swp391.model.Points;
import jakarta.persistence.Transient;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class UserRespone {
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private EnumGenderName gender;
    private Date yearOfBirth;
    @JsonIgnore
    private List<Points> points;

    public double getTotalPoints() {
        if (points == null) {
            return 0;
        }
        return points.stream().mapToDouble(Points::getPoint).sum();
    }

    public double getTotalUsedPoints() {
        if (points == null) {
            return 0;
        }
        return points.stream().mapToDouble(Points::getUsedPoints).sum();
    }
}
