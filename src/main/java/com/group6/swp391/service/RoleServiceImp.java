package com.group6.swp391.service;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.model.Role;
import com.group6.swp391.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImp implements RoleService {

    @Autowired private RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(EnumRoleName roleName) {
        Role role = roleRepository.getRoleByRoleName(roleName);
        return role;
    }
}
