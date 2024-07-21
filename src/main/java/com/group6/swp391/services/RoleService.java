package com.group6.swp391.services;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.pojos.Role;

import java.util.List;

public interface RoleService {
    public Role getRoleByRoleName(EnumRoleName roleName);

    public List<Role> getAllRoles();
}
