package com.group6.swp391.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;
    @Column(name = "first_name", length = 20)
    private String firstName;
    @Column(name = "last_name", length = 20)
    private String lastName;
    @Column(name = "email", nullable = false, length = 200, unique = true)
    private String email;
    @Column(name = "password", nullable = false, length = 200)
    private String password;
    @Column(name = "phone", length = 20)
    private String phone;
    @Column(name = "address", length = 200)
    private String address;
    @Column(name = "avata")
    private String avata;
    @Column(name = "code_verify", nullable = false)
    private String codeVerify;
    private boolean enabled;
    private boolean looked;
    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "userID"), inverseJoinColumns = @JoinColumn(name = "roleID"))
    private Set<Role> roles = new HashSet<>();

    public User(String firstName, String lastName, String email, String password, String phone, String address, String avata, String codeVerify, boolean enabled, boolean looked, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.avata = avata;
        this.codeVerify = codeVerify;
        this.enabled = enabled;
        this.looked = looked;
        this.roles = roles;
    }
}
