package com.group6.swp391.repositories;

import com.group6.swp391.enums.EnumRoleName;
import com.group6.swp391.pojos.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Role getRoleByRoleName(EnumRoleName role);
}
