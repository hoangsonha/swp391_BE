package com.group6.swp391.config;

import com.group6.swp391.pojos.Role;
import com.group6.swp391.pojos.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Builder
public class CustomUserDetail implements UserDetails {
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String avata;
    private boolean nonLocked;
    private boolean enabled;
    private Collection<GrantedAuthority> grantedAuthorities;

    public static CustomUserDetail mapUserToUserDetail(User user) {
//        List<GrantedAuthority> roles = user.getRole().map(
//                role -> new SimpleGrantedAuthority(role.getRole().name())
//                ).collect(Collectors.toList());

        Role role = user.getRole();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getRoleName().name());
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(simpleGrantedAuthority);

        CustomUserDetail customUserDetail = CustomUserDetail.builder()
                .userID(user.getUserID())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .address(user.getAddress())
                .avata(user.getAvata())
                .nonLocked(user.isNonLocked())
                .enabled(user.isEnabled())
                .grantedAuthorities(roles)
                .build();
        return customUserDetail;

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
