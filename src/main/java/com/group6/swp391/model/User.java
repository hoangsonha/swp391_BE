package com.group6.swp391.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.group6.swp391.enums.EnumGenderName;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;
    @Column(name = "first_name", columnDefinition = "nvarchar(20)")
    private String firstName;
    @Column(name = "last_name", columnDefinition = "nvarchar(20)")
    private String lastName;
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;
    @Column(name = "password", length = 100)
    private String password;
    @Column(name = "phone", length = 15)
    private String phone;
    @Column(name = "address", columnDefinition = "varchar(300)")
    private String address;
    @Column(name = "avata")
    private String avata;
    @Column(name = "code_verify", nullable = false)
    private String codeVerify;
    private boolean enabled;

    @Column(name = "non_locked")
    private boolean nonLocked;

    @Column(name= "gender")
    @Enumerated(EnumType.STRING)
    private EnumGenderName gender;

    @Column(name= "year_of_birth")
    private Date yearOfBirth;

    @Column(name = "offline_at")
    private Date offlineAt;

    private int quantityLoginFailed;

    @DateTimeFormat(pattern = "YYYY-MM-DD hh:mm:ss")
    private Date timeLoginFailed;

    @ManyToOne
    @JoinColumn(name = "roleID")
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    @JsonIgnore
    private List<Points> points;

    @Transient
    public double getTotalPoints() {
        if (points == null) {
            return 0;
        }
        return points.stream().mapToDouble(Points::getPoint).sum();
    }

    @Transient
    public double getTotalUsedPoints() {
        if (points == null) {
            return 0;
        }
        return points.stream().mapToDouble(Points::getUsedPoints).sum();
    }


    public User(String firstName, String lastName, String email, String password, String phone, String address,
                String avata, String codeVerify, boolean enabled, boolean nonLooked, Role role, int quantityLoginFailed,
                Date timeLoginFailed, EnumGenderName gender, Date yearOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.avata = avata;
        this.codeVerify = codeVerify;
        this.enabled = enabled;
        this.nonLocked = nonLooked;
        this.role = role;
        this.quantityLoginFailed = quantityLoginFailed;
        this.gender = gender;
        this.yearOfBirth = yearOfBirth;
        this.timeLoginFailed = timeLoginFailed;
    }
}
