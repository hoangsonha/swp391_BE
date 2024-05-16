package com.group6.swp391.repository;

import com.group6.swp391.model.EnumRoleName;
import com.group6.swp391.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Role getRoleByRoleName(EnumRoleName role);
}
