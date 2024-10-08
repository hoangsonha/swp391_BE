package com.group6.swp391.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.group6.swp391.enums.EnumGenderName;
import com.group6.swp391.pojos.Points;
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

    @Transient
    @JsonProperty("totalPoints")
    public double getNetPoints() {
        if (points == null) {
            return 0;
        }
        double totalPoints = points.stream().mapToDouble(Points::getPoint).sum();
        double totalUsedPoints = points.stream().mapToDouble(Points::getUsedPoints).sum();
        return totalPoints - totalUsedPoints;
    }
}
