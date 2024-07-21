package com.group6.swp391.services;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.pojos.Role;
import com.group6.swp391.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImp implements RoleService {

    @Autowired private RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(EnumRoleName roleName) {
        Role role = roleRepository.getRoleByRoleName(roleName);
        return role;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
