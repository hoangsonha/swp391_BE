package com.group6.swp391.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Column(name = "first_name", columnDefinition = "NVARCHAR(30)")
    private String firstName;
    @Column(name = "last_name", columnDefinition = "NVARCHAR(30)")
    private String lastName;
    @Column(name = "email", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String email;
    @Column(name = "password", nullable = true, columnDefinition = "VARCHAR(60)")
    private String password;
    @Column(name = "phone", nullable = true, unique = true, columnDefinition = "VARCHAR(12)")
    private String phone;
    @Column(name = "address", nullable = true, columnDefinition = "NVARCHAR(255)")
    private String address;
    @Column(name = "avata", nullable = true, columnDefinition = "VARCHAR(255)")
    private String avata;
    @Column(name = "code_verify", nullable = false, columnDefinition = "VARCHAR(36)")
    private String codeVerify;
    @Column(name = "enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean enabled;

    @Column(name = "non_locked", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean nonLocked;

    @Column(name= "gender", nullable = true , columnDefinition = "VARCHAR(6)")
    @Enumerated(EnumType.STRING)
    private EnumGenderName gender;

    @Column(name= "year_of_birth", nullable = true)
    private Date yearOfBirth;

    @Column(name = "offline_at", nullable = true)
    private Date offlineAt;

    @Column(columnDefinition = "INT DEFAULT 0")
    private int quantityLoginFailed;

    @Column(name = "number_of_receive_email_offline", columnDefinition = "INT DEFAULT 0")
    private int numberOFReceiveEmailOffline;

    @DateTimeFormat(pattern = "YYYY-MM-DD hh:mm:ss")
    @Column(name = "time_login_failed", nullable = true)
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
    private String roleName;


    @Transient
    public double getTotalUsedPoints() {
        if (points == null) {
            return 0;
        }
        return points.stream().mapToDouble(Points::getUsedPoints).sum();
    }


    public User(String firstName, String lastName, String email, String password, String phone, String address,
                String avata, String codeVerify, boolean enabled, boolean nonLooked, Role role, int quantityLoginFailed,
                Date timeLoginFailed, EnumGenderName gender, Date yearOfBirth, int receiveEmailOffline) {
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
        this.numberOFReceiveEmailOffline = receiveEmailOffline;
    }

}
