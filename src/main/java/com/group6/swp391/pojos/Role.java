package com.group6.swp391.pojos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group6.swp391.enums.EnumRoleName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "role")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleID;
    @Enumerated(EnumType.STRING)
    private EnumRoleName roleName;
    @JsonBackReference
    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();
    public Role(EnumRoleName roleName, Set<User> users) {
        this.roleName = roleName;
        this.users = users;
    }
}
