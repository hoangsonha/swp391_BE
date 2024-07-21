package com.group6.swp391.mapper;

import com.group6.swp391.config.CustomUserDetail;
import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.pojos.Role;
import com.group6.swp391.pojos.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class UserMapper {

    public static User customUserDetailToUser(CustomUserDetail customUserDetail) {

        Collection<GrantedAuthority> grantedAuthorities = customUserDetail.getGrantedAuthorities();

        List<EnumRoleName> roles= grantedAuthorities.stream()
                .map(authorityName -> {
                    String roleName = authorityName.getAuthority();
                    return EnumRoleName.valueOf(roleName);
                }).collect(Collectors.toList());

        Role role = new Role();
        for(EnumRoleName role_enum : roles) {
            role.setRoleName(role_enum);
        }

        User user = User.builder()
                .userID(customUserDetail.getUserID())
                .email(customUserDetail.getEmail())
                .address(customUserDetail.getAddress())
                .avata(customUserDetail.getAvata())
                .firstName(customUserDetail.getFirstName())
                .lastName(customUserDetail.getLastName())
                .password(customUserDetail.getPassword())
                .phone(customUserDetail.getPhone())
                .nonLocked(customUserDetail.isNonLocked())
                .enabled(customUserDetail.isEnabled())
                .role(role)
                .build();
        return user;
    }
}
