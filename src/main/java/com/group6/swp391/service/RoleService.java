package com.group6.swp391.service;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.model.Role;

public interface RoleService {
    public Role getRoleByRoleName(EnumRoleName roleName);
}
